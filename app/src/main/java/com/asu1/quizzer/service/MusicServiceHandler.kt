package com.asu1.quizzer.service

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.musics.MediaStateEvents
import com.asu1.quizzer.util.musics.MusicStates
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicServiceHandler(
    private val exoPlayer: ExoPlayer
): Player.Listener{

    private var _musicStates: MutableStateFlow<MusicStates> = MutableStateFlow(MusicStates.Initial)
    val musicStates: StateFlow<MusicStates> = _musicStates.asStateFlow()

    private var _currentProgress: MutableStateFlow<Long> = MutableStateFlow(0L)
    val currentProgress: StateFlow<Long> = _currentProgress.asStateFlow()

    private var job: Job? = null

    init{
        exoPlayer.addListener(this)
    }

    fun setMediaItem(mediaItem: MediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun setMediaItemList(mediaItems: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    suspend fun onMediaStateEvents(
        mediaStateEvents: MediaStateEvents,
        selectedMusicIndex: Int = -1,
        seekPosition: Long = 0,
    ) {
        when (mediaStateEvents) {
            MediaStateEvents.Backward -> exoPlayer.seekBack()
            MediaStateEvents.Forward -> exoPlayer.seekForward()
            MediaStateEvents.PlayPause -> playPauseMusic()
            MediaStateEvents.SeekTo -> exoPlayer.seekTo(seekPosition)
            MediaStateEvents.Play -> playMusic()
            MediaStateEvents.Pause -> pauseMusic()
            MediaStateEvents.SeekToNext -> {
                if (exoPlayer.currentMediaItemIndex == exoPlayer.mediaItemCount - 1) {
                    exoPlayer.seekToDefaultPosition(0) // Jump to the first item
                } else {
                    exoPlayer.seekToNext()
                }
            }
            MediaStateEvents.SeekToPrevious -> {
                if(exoPlayer.currentMediaItemIndex == 0) {
                    exoPlayer.seekToDefaultPosition(exoPlayer.mediaItemCount - 1) // Jump to the last item
                } else exoPlayer.seekToPrevious()
            }
            MediaStateEvents.Stop -> stopProgressUpdate()
            MediaStateEvents.SelectedMusicChange -> {
                when (selectedMusicIndex) {
                    exoPlayer.currentMediaItemIndex -> {
                        playPauseMusic()
                    }

                    else -> {
                        exoPlayer.seekToDefaultPosition(selectedMusicIndex)
                        _musicStates.value = MusicStates.MediaPlaying(
                            isPlaying = true
                        )
                        exoPlayer.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }

            is MediaStateEvents.MediaProgress -> {
                exoPlayer.seekTo(
                    (exoPlayer.duration * mediaStateEvents.progress).toLong()
                )
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _musicStates.value =
                MusicStates.MediaBuffering(exoPlayer.currentPosition)

            ExoPlayer.STATE_READY -> {
                _musicStates.value = MusicStates.MediaReady(exoPlayer.duration)
                _musicStates.value = MusicStates.CurrentMediaPlaying(exoPlayer.currentMediaItemIndex)
            }

            Player.STATE_ENDED -> {
                // no-op
            }

            Player.STATE_IDLE -> {
                // no-op
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (exoPlayer.playbackState != ExoPlayer.STATE_BUFFERING) {
            _musicStates.value = MusicStates.MediaPlaying(isPlaying = isPlaying)
            _musicStates.value = MusicStates.CurrentMediaPlaying(exoPlayer.currentMediaItemIndex)
            if (isPlaying) {
                GlobalScope.launch(Dispatchers.Main) {
                    startProgressUpdate()
                }
            } else {
                stopProgressUpdate()
            }
        }
    }

    private suspend fun playPauseMusic() {
        if (exoPlayer.isPlaying) {
            pauseMusic()
        } else {
            playMusic()
        }
    }

    private suspend fun playMusic(){
        exoPlayer.play()
        _musicStates.value = MusicStates.MediaPlaying(
            isPlaying = true
        )
        startProgressUpdate()
    }

    private suspend fun pauseMusic(){
        exoPlayer.pause()
        stopProgressUpdate()
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(1000)
            _musicStates.value = MusicStates.MediaProgress(exoPlayer.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _musicStates.value = MusicStates.MediaPlaying(isPlaying = false)
    }

}