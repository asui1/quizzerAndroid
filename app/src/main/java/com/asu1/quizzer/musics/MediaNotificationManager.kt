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
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.asu1.quizzer.R
import com.asu1.quizzer.util.constants.NOTIFICATION_CHANNEL_ID
import com.asu1.quizzer.util.constants.NOTIFICATION_CHANNEL_NAME
import com.asu1.quizzer.util.constants.NOTIFICATION_ID

@OptIn(UnstableApi::class)
class MediaNotificationManager
    (
    private val context: Context,
    private val exoPlayer: Player
) {
    private val musicNotificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)
    private var playerNotificationManager: PlayerNotificationManager? = null

    init {
        createMusicNotificationChannel()
    }

    private fun createMusicNotificationChannel() {
        val musicNotificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        musicNotificationManager.createNotificationChannel(musicNotificationChannel)
    }

    @UnstableApi
    private fun buildMusicNotification(mediaSession: MediaSession) {
        playerNotificationManager = PlayerNotificationManager.Builder(
            context,
            NOTIFICATION_ID,
            NOTIFICATION_CHANNEL_ID
        )
            .setMediaDescriptionAdapter(
                MusicNotificationDescriptorAdapter(
                    context = context,
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
        buildMusicNotification(mediaSession)
        startForegroundMusicService(mediaSessionService)
    }

    private fun startForegroundMusicService(mediaSessionService: MediaSessionService) {
        val musicNotification = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setSmallIcon(R.drawable.quizzerresize2) // Ensure you have a valid small icon
            .setContentTitle("Music Playback") // Set a title for the notification
            .setContentText("Playing music") // Set content text for the notification
            .build()

        mediaSessionService.startForeground(NOTIFICATION_ID, musicNotification)
    }
}