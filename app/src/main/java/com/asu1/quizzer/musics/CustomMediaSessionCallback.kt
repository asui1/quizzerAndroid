package com.asu1.quizzer.musics

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.asu1.resources.MusicNotificationButtons
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class CustomMediaSessionCallback : MediaSession.Callback {
    @OptIn(UnstableApi::class)
    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        val sessionCommandBuilder = MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()

        MusicNotificationButtons.entries.forEach{command ->
            sessionCommandBuilder.add(command.id)
        }

        val commands = MusicNotificationButtons.entries.map {
            CommandButton.Builder()
                .setIconResId(it.icon)
                .setDisplayName(it.displayName)
                .setSessionCommand(it.command)
                .build()
        }

        return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
            .setAvailableSessionCommands(sessionCommandBuilder.build())
            .setCustomLayout(commands)
            .build()
    }

    @OptIn(UnstableApi::class)
    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        when(customCommand.commandCode){
            MusicNotificationButtons.PLAY_PAUSE.id -> {
                // Play or pause music
            }
            MusicNotificationButtons.NEXT.id -> {
                // Skip to next music
            }
            MusicNotificationButtons.PREVIOUS.id -> {
                // Skip to previous music
            }
            MusicNotificationButtons.REWIND.id -> {
                // Rewind music
            }
            MusicNotificationButtons.FORWARD.id -> {
                // Forward music
            }
            else -> {
            }
        }
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }
}