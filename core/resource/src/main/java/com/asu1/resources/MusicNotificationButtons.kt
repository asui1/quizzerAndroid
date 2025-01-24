package com.asu1.resources

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionCommand.COMMAND_CODE_CUSTOM

@UnstableApi
enum class MusicNotificationButtons(val id: Int, val displayName: String, val icon: Int, val command: SessionCommand) {
    PREVIOUS(0, "Previous", CommandButton.ICON_PREVIOUS, SessionCommand(COMMAND_CODE_CUSTOM)),
    REWIND(1, "Rewind", CommandButton.ICON_REWIND, SessionCommand(COMMAND_CODE_CUSTOM)),
    PLAY_PAUSE(2, "Play/Pause", CommandButton.ICON_PLAY, SessionCommand(COMMAND_CODE_CUSTOM)),
    FORWARD(3, "Forward", CommandButton.ICON_FAST_FORWARD, SessionCommand(COMMAND_CODE_CUSTOM)),
    NEXT(4, "Next", CommandButton.ICON_NEXT, SessionCommand(COMMAND_CODE_CUSTOM));
}
