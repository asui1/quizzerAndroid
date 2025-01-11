package com.asu1.quizzer.viewModels

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.asu1.quizzer.musics.MediaNotificationManager
import com.asu1.quizzer.musics.Music
import com.asu1.quizzer.musics.MusicAllInOne
import com.asu1.quizzer.util.musics.ControlButtons
import com.asu1.quizzer.util.musics.PlayerUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(UnstableApi::class)
@HiltViewModel
class MusicListViewModel @Inject constructor(
    val player: ExoPlayer
): ViewModel(){

    private val _currentPlayingIndex = MutableStateFlow(0)
    val currentPlayingIndex = _currentPlayingIndex.asStateFlow()

    private val _totalDurationInMS = MutableStateFlow(0L)
    val totalDurationInMS = _totalDurationInMS.asStateFlow()

    private val _currentDurationInMs = MutableStateFlow(0L)
    val currentDurationInMs = _currentDurationInMs.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _playlist: MutableStateFlow<List<MusicAllInOne>> = MutableStateFlow(
        listOf(
            MusicAllInOne(
                music = Music("sample1", "test1"),
                moods = setOf("Happy")
            ),
            MusicAllInOne(
                music = Music("sample2", "test2"),
                moods = setOf("Scary")
            ),
        )
    )
    val playlist = _playlist.asStateFlow()

    private val _playerUiState: MutableStateFlow<PlayerUIState> =
        MutableStateFlow(PlayerUIState.Loading)
    val playerUiState: StateFlow<PlayerUIState> = _playerUiState.asStateFlow()

    private lateinit var notificationManager: MediaNotificationManager

    protected lateinit var mediaSession: MediaSession
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    companion object {
        private const val SESSION_INTENT_REQUEST_CODE = 1001
    }

    private var isStarted = false

    fun preparePlayer(context: Context) {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        player.setAudioAttributes(audioAttributes, true)
        player.repeatMode = Player.REPEAT_MODE_ALL

        player.addListener(playerListener)

        setupPlaylist(context)
    }

    private fun setupPlaylist(context: Context) {

        val videoItems: ArrayList<MediaSource> = arrayListOf()
        _playlist.value.forEach { music ->
            val mediaMetaData = MediaMetadata.Builder()
                .setTitle(music.music.title)
                .setAlbumArtist(music.music.artist)
                .build()

            val trackUri = Uri.parse(music.getUri())
            val mediaItem = MediaItem.Builder()
                .setUri(trackUri)
                .setMediaMetadata(mediaMetaData)
                .build()
            val dataSourceFactory = DefaultDataSource.Factory(context)

            val mediaSource =
                DashMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

            videoItems.add(
                mediaSource
            )
        }

        onStart(context)

        player.playWhenReady = false
        player.setMediaSources(videoItems)
        player.prepare()
    }

    fun updatePlayer(action: ControlButtons) {
        when (action) {
            ControlButtons.PlayPause -> if (player.isPlaying) player.pause() else player.play()
            ControlButtons.Next -> player.seekToNextMediaItem()
            ControlButtons.Rewind -> player.seekToPreviousMediaItem()
        }
    }

    fun updatePlayerPosition(position: Long) {
        player.seekTo(position)
    }

    fun onStart(context: Context) {
        if (isStarted) return

        isStarted = true

        // Build a PendingIntent that can be used to launch the UI.
        val sessionActivityPendingIntent =
            context.packageManager?.getLaunchIntentForPackage(context.packageName)
                ?.let { sessionIntent ->
                    PendingIntent.getActivity(
                        context,
                        SESSION_INTENT_REQUEST_CODE,
                        sessionIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                }

        // Create a new MediaSession.
        mediaSession = MediaSession.Builder(context, player)
            .setSessionActivity(sessionActivityPendingIntent!!).build()

        notificationManager =
            MediaNotificationManager(
                context,
                mediaSession.token,
                player,
                PlayerNotificationListener()
            )

        notificationManager.showNotificationForPlayer(player)
    }

    /**
     * Destroy audio notification
     */
    fun onDestroy() {
        onClose()
        player.release()
    }

    /**
     * Close audio notification
     */
    private fun onClose() {
        if (!isStarted) return

        isStarted = false
        mediaSession.run {
            release()
        }

        // Hide notification
        notificationManager.hideNotification()

        // Free ExoPlayer resources.
        player.removeListener(playerListener)
    }

    /**
     * Listen for notification events.
     */
    private inner class PlayerNotificationListener :
        PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {

        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {

        }
    }

    /**
     * Listen to events from ExoPlayer.
     */
    private val playerListener = object : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            syncPlayerFlows()
            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_READY -> {
                    notificationManager.showNotificationForPlayer(player)
                }

                else -> {
                    notificationManager.hideNotification()
                }
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            syncPlayerFlows()
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            _isPlaying.value = isPlaying
            if (isPlaying) {
                startUpdatingPosition()
            } else {
                stopUpdatingPosition()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
        }
    }

    private fun startUpdatingPosition(){
        serviceScope.launch {
            while (player.isPlaying){
                _currentDurationInMs.value = player.currentPosition
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    private fun stopUpdatingPosition(){
        serviceScope.coroutineContext.cancelChildren()
    }

    private fun syncPlayerFlows() {
        _currentPlayingIndex.value = player.currentMediaItemIndex
        _totalDurationInMS.value = player.duration.coerceAtLeast(0L)
    }

}