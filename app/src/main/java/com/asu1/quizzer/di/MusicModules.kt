package com.asu1.quizzer.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.asu1.quizzer.musics.MediaNotificationManager
import com.asu1.quizzer.service.MusicServiceHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicModules {

    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .setTrackSelector(DefaultTrackSelector(context))
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
    }

    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        exoPlayer: ExoPlayer
    ): MediaSession {
        return MediaSession.Builder(context, exoPlayer).build()
    }

    @Provides
    @Singleton
    fun provideMediaNotificationManager(
        @ApplicationContext context: Context,
        exoPlayer: ExoPlayer
    ): MediaNotificationManager {
        return MediaNotificationManager(
            context = context,
            exoPlayer = exoPlayer
        )
    }

    @Provides
    @Singleton
    fun provideMusicServiceHandler(
        exoPlayer: ExoPlayer
    ): MusicServiceHandler {
        return MusicServiceHandler(exoPlayer)
    }

}