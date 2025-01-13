package com.asu1.quizzer.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.asu1.quizzer.musics.Music
import com.asu1.quizzer.musics.MusicAllInOne
import com.asu1.quizzer.service.MusicServiceHandler
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.musics.HomeUIState
import com.asu1.quizzer.util.musics.HomeUiEvents
import com.asu1.quizzer.util.musics.MediaStateEvents
import com.asu1.quizzer.util.musics.MusicStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS


@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class MusicListViewModel @Inject constructor(
    val musicServiceHandler: MusicServiceHandler,
    val savedStateHandle: SavedStateHandle,
): ViewModel(){

    var duration by savedStateHandle.saveable { mutableLongStateOf(0L) }


    // PROGRESS IN VALUE FROM 0f to 1f, showing progress * duration.
    private val _progress = MutableLiveData(0f)
    val progress: LiveData<Float> get() = _progress

    var isMusicPlaying by savedStateHandle.saveable { mutableStateOf(false) }

    var currentSelectedMusic by mutableStateOf(
        MusicAllInOne(
            music = Music(
                title = "sample1",
                artist = "Test1",
            ),
            moods = setOf("Happy")
        )
    )

    var musicList by mutableStateOf(listOf<MusicAllInOne>(
        MusicAllInOne(
            music = Music(
                title = "sample1",
                artist = "Test1",
            ),
            moods = setOf("Happy")
        ),
        MusicAllInOne(
            music = Music(
                title = "sample2",
                artist = "Test2",
            ),
            moods = setOf("Happy")
        ),
    ))

    private val _homeUiState: MutableStateFlow<HomeUIState> =
        MutableStateFlow(HomeUIState.InitialHome)
    val homeUIState: StateFlow<HomeUIState> = _homeUiState.asStateFlow()

    init{
        setMusicItems()
    }

    init {
        viewModelScope.launch {
            musicServiceHandler.musicStates.collectLatest { musicStates: MusicStates ->
                when (musicStates) {
                    MusicStates.Initial -> _homeUiState.value = HomeUIState.InitialHome
                    is MusicStates.MediaBuffering -> progressCalculation(musicStates.progress)
                    is MusicStates.MediaPlaying -> isMusicPlaying = musicStates.isPlaying
                    is MusicStates.MediaProgress -> progressCalculation(musicStates.progress)
                    is MusicStates.CurrentMediaPlaying -> {
                        Logger.debug("MUSIC STATE Current Media Playing ${musicStates.mediaItemIndex}")
                        currentSelectedMusic = musicList[musicStates.mediaItemIndex]
                    }

                    is MusicStates.MediaReady -> {
                        Logger.debug("MUSIC STATE MediaReady ${musicStates.duration}")
                        duration = musicStates.duration
                        _homeUiState.value = HomeUIState.HomeReady
                    }
                }
            }
        }
    }

    fun onHomeUiEvents(homeUiEvents: HomeUiEvents) = viewModelScope.launch {
        when (homeUiEvents) {
            HomeUiEvents.Backward -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.Backward)
            HomeUiEvents.Forward -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.Forward)
            HomeUiEvents.SeekToNext -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.SeekToNext)
            HomeUiEvents.SeekToPrevious -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.SeekToPrevious)
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

            is HomeUiEvents.CurrentAudioChanged -> {
                musicServiceHandler.onMediaStateEvents(
                    MediaStateEvents.SelectedMusicChange,
                    selectedMusicIndex = homeUiEvents.index
                )
            }

            is HomeUiEvents.UpdateProgress -> {
                musicServiceHandler.onMediaStateEvents(
                    MediaStateEvents.MediaProgress(
                        homeUiEvents.progress
                    )
                )
                _progress.postValue(homeUiEvents.progress)
            }
            is HomeUiEvents.UpdateLocalProgress -> {
                _progress.postValue(homeUiEvents.progress)
            }

        }
    }

    private fun setMusicItems() {
        musicList.map { audioItem ->
            MediaItem.Builder()
                .setUri(audioItem.getUri())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(audioItem.music.artist)
                        .setDisplayTitle(audioItem.music.title)
                        .build()
                )
                .build()
        }.also {mediaItem ->
            musicServiceHandler.setMediaItemList(mediaItem)
        }
    }

    private fun progressCalculation(currentProgress: Long) {
        _progress.postValue(
            if (currentProgress > 0) (currentProgress.toFloat() / duration.toFloat())
            else 0f
        )

    }

    private fun formatDurationValue(duration: Long): String {
        val minutes = MINUTES.convert(duration, MILLISECONDS)
        val seconds = (minutes) - minutes * SECONDS.convert(1, MINUTES)

        return String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        viewModelScope.launch {
            musicServiceHandler.onMediaStateEvents(MediaStateEvents.Stop)
        }
        super.onCleared()
    }
}