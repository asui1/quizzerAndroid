package com.asu1.quizzer.musics

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.asu1.resources.NOTIFICATION_CHANNEL_ID
import com.asu1.resources.NOTIFICATION_CHANNEL_NAME
import com.asu1.resources.NOTIFICATION_ID
import com.asu1.resources.R

@OptIn(UnstableApi::class)
class MediaNotificationManager
    (
    private val context: Context,
    private val exoPlayer: Player
) {
    private var musicNotificationManager: NotificationManagerCompat? = null
    private var playerNotificationManager: PlayerNotificationManager? = null

    private fun createMusicNotificationChannel(
        mediaSessionService: MediaSessionService,
    ) {
        val musicNotificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        musicNotificationManager = NotificationManagerCompat.from(mediaSessionService)
        musicNotificationManager!!.createNotificationChannel(musicNotificationChannel)
    }

    @UnstableApi
    private fun buildMusicNotification(
        mediaSessionService: MediaSessionService,
        mediaSession: MediaSession) {
        playerNotificationManager = PlayerNotificationManager.Builder(
            mediaSessionService,
            NOTIFICATION_ID,
            NOTIFICATION_CHANNEL_ID
        )
            .setMediaDescriptionAdapter(
                MusicNotificationDescriptorAdapter(
                    context = mediaSessionService,
                    pendingIntent = mediaSession.sessionActivity
                )
            )
            .setSmallIconResourceId(R.drawable.quizzerresize2)
            .build()
            .also {
                it.setMediaSessionToken(mediaSession.platformToken)
                it.setUseFastForwardActionInCompactView(true)
                it.setUseRewindActionInCompactView(true)
                it.setUseNextActionInCompactView(true)
                it.setUsePreviousActionInCompactView(true)
                it.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                it.setPlayer(exoPlayer)
            }
    }

    @UnstableApi
    fun startMusicNotificationService(
        mediaSessionService: MediaSessionService,
        mediaSession: MediaSession
    ) {
        if(musicNotificationManager == null) {
            createMusicNotificationChannel(mediaSessionService)
            buildMusicNotification(mediaSessionService, mediaSession)
        }
        startForegroundMusicService(mediaSessionService)
    }

    private fun startForegroundMusicService(mediaSessionService: MediaSessionService) {
        val musicNotification = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentTitle("Music Playback") // Set a title for the notification
            .setContentText("Playing music") // Set content text for the notification
            .build()

        mediaSessionService.startForeground(NOTIFICATION_ID, musicNotification)
    }
}