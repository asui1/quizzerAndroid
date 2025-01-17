package com.asu1.quizzer.viewModels

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.Util.startForegroundService
import com.asu1.quizzer.musics.MusicAllInOne
import com.asu1.quizzer.service.MusicServiceHandler
import com.asu1.quizzer.service.QuizzerMusicPlayerService
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.constants.sampleMusicList
import com.asu1.quizzer.util.musics.HomeUIState
import com.asu1.quizzer.util.musics.HomeUiEvents
import com.asu1.quizzer.util.musics.MediaStateEvents
import com.asu1.quizzer.util.musics.MusicStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

//TODO: MUSIC STATE SHOULD BE BASED ON SERVICE Escpecially on Init.(Current Init overrides service even if it is running.)
@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class MusicListViewModel @Inject constructor(
    private val musicServiceHandler: MusicServiceHandler,
    private val savedStateHandle: SavedStateHandle,
): ViewModel(){

    private var isMusicServiceRunning by savedStateHandle.saveable { mutableStateOf(false) }

    var duration by savedStateHandle.saveable { mutableLongStateOf(0L) }

    // PROGRESS IN VALUE FROM 0f to 1f, showing progress * duration.
    private val _progress = MutableLiveData(0f)
    val progress: LiveData<Float> get() = _progress

    private val progressMutex = Mutex()
    private val updateProgressMutex = Mutex()

    var isMusicPlaying by savedStateHandle.saveable { mutableStateOf(false) }

    private val _currentMusicIndex = MutableLiveData(0)
    val currentMusicIndex: LiveData<Int> get() = _currentMusicIndex


    private var _musicList = MutableStateFlow<MutableList<MusicAllInOne>>(sampleMusicList.toMutableList())
    val musicList: StateFlow<MutableList<MusicAllInOne>> get() = _musicList

    private val _homeUiState: MutableStateFlow<HomeUIState> =
        MutableStateFlow(HomeUIState.InitialHome)

    //TODO: VIEWMODEL이 새로 시작될 때 리셋되버림.
    init{
        Logger.debug("MusicListViewModel created1")
        setMusicItems()
    }

    init {
        Logger.debug("MusicListViewModel created2")
        viewModelScope.launch {
            musicServiceHandler.musicStates.collectLatest { musicStates: MusicStates ->
                when (musicStates) {
                    MusicStates.Initial -> _homeUiState.value = HomeUIState.InitialHome
                    is MusicStates.MediaBuffering -> {
                        progressCalculation(musicStates.progress)
                    }
                    is MusicStates.MediaPlaying -> isMusicPlaying = musicStates.isPlaying
                    is MusicStates.MediaProgress -> {
                        progressCalculation(musicStates.progress)
                    }
                    is MusicStates.CurrentMediaPlaying -> {
                        _currentMusicIndex.postValue(musicStates.mediaItemIndex)
                    }

                    is MusicStates.MediaReady -> {
                        duration = musicStates.duration
                        _homeUiState.value = HomeUIState.HomeReady
                    }
                }
            }
        }
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun startMusicService(context: Context){
        if(!isMusicServiceRunning){
            val intent = Intent(context, QuizzerMusicPlayerService::class.java)
            startForegroundService(context, intent)
            isMusicServiceRunning = true
        }
    }

    fun onHomeUiEvents(homeUiEvents: HomeUiEvents) = viewModelScope.launch {
        when (homeUiEvents) {
            HomeUiEvents.Backward -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.Backward)
            HomeUiEvents.Forward -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.Forward)
            HomeUiEvents.SeekToNext -> {
                if(_currentMusicIndex.value != null)
                    _currentMusicIndex.postValue((_currentMusicIndex.value!! + 1) % _musicList.value.size)
                musicServiceHandler.onMediaStateEvents(MediaStateEvents.SeekToNext)
            }
            HomeUiEvents.SeekToPrevious -> {
                Logger.debug("Seeking to previous ${_progress.value}")
                if(_progress.value!! * duration > 4000){
                    musicServiceHandler.onMediaStateEvents(MediaStateEvents.SeekTo, seekPosition = 0)
                    return@launch
                }
                else {
                    if (_currentMusicIndex.value != null) {
                        val newIndex = if (_currentMusicIndex.value!! == 0) {
                            _musicList.value.size - 1
                        } else {
                            (_currentMusicIndex.value!! - 1) % _musicList.value.size
                        }
                        _currentMusicIndex.postValue(newIndex)
                    }
                    musicServiceHandler.onMediaStateEvents(MediaStateEvents.SeekToPrevious)
                }
            }
            HomeUiEvents.Pause -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.Pause)
            HomeUiEvents.Play -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.Play)
            is HomeUiEvents.PlayPause -> {
                musicServiceHandler.onMediaStateEvents(
                    MediaStateEvents.PlayPause
                )
            }
            is HomeUiEvents.SeekTo -> {
                musicServiceHandler.onMediaStateEvents(
                    MediaStateEvents.SeekTo,
                    seekPosition = ((duration * homeUiEvents.position) / 100f).toLong()
                )
            }
            is HomeUiEvents.ChangeItemOrder -> {
                Logger.debug("Changing item order from ${homeUiEvents.from} to ${homeUiEvents.to}")
                val musicListSize = _musicList.value.size
                if (homeUiEvents.from in 0 until musicListSize && homeUiEvents.to in 0 until musicListSize) {
                    val curMusic = _musicList.value[_currentMusicIndex.value!!]
                    _musicList.value.apply {
                        val item = removeAt(homeUiEvents.from)
                        add(homeUiEvents.to, item)
                    }
                    _currentMusicIndex.value = _musicList.value.indexOf(curMusic)
                    musicServiceHandler.onMediaStateEvents(
                        MediaStateEvents.ChangeItemOrder(
                            homeUiEvents.from,
                            homeUiEvents.to
                        )
                    )
                } else {
                    Logger.debug("Invalid indices: from=${homeUiEvents.from}, to=${homeUiEvents.to}")
                }
            }
            is HomeUiEvents.CurrentAudioChanged -> {
                 if(homeUiEvents.index in 0 until _musicList.value.size){
                     _currentMusicIndex.value = homeUiEvents.index
                     musicServiceHandler.onMediaStateEvents(
                         MediaStateEvents.SelectedMusicChange,
                         selectedMusicIndex = homeUiEvents.index
                     )
                 }
            }

            is HomeUiEvents.UpdateProgress -> {
                tryProgressUpdate(homeUiEvents.progress)
                musicServiceHandler.onMediaStateEvents(
                    MediaStateEvents.MediaProgress(
                        homeUiEvents.progress
                    )
                )
            }
            is HomeUiEvents.AddLocalProgress -> {
                addProgressWithMutex(homeUiEvents.progress)
            }
            is HomeUiEvents.UpdateLocalProgress -> {
                updateProgressMutex.lock()
                updateProgressWithMutex(homeUiEvents.progress)
            }
            HomeUiEvents.PushLocalProgress -> {
                updateProgressMutex.unlock()
                musicServiceHandler.onMediaStateEvents(
                    MediaStateEvents.MediaProgress(_progress.value ?: 0f)
                )
            }
        }
    }

    private fun tryProgressUpdate(progress: Float) {
        if(!updateProgressMutex.isLocked){
            _progress.postValue(progress)
        }
    }

    private suspend fun updateProgressWithMutex(progress: Float) {
        progressMutex.withLock {
            withContext(Dispatchers.Main) {
                _progress.value = progress
                Logger.debug("Progress updated with mutex $progress")
            }
        }
    }

    private suspend fun addProgressWithMutex(dragProgress: Float) {
        progressMutex.withLock {
            val newProgress = _progress.value?.plus(dragProgress)?.coerceIn(0f, 1f)
            if(newProgress != null) _progress.postValue(newProgress)
        }
    }

    private fun setMusicItems() {
        _musicList.value.map { audioItem ->
            MediaItem.Builder()
                .setUri(audioItem.getUri())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(audioItem.music.artist)
                        .setDisplayTitle(audioItem.music.title)
                        .build()
                )
                .build()
        }.also { mediaItem ->
            musicServiceHandler.setMediaItemList(mediaItem)
        }
    }

    private fun progressCalculation(currentProgress: Long) {
        val duration = if (currentProgress > 0) (currentProgress.toFloat() / duration.toFloat())
        else 0f
        tryProgressUpdate(duration)
    }

    override fun onCleared() {
        viewModelScope.launch {
            musicServiceHandler.onMediaStateEvents(MediaStateEvents.Stop)
        }
        super.onCleared()
    }
}