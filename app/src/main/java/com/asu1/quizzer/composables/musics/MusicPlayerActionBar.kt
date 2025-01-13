package com.asu1.quizzer.composables.musics

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.util.musics.ControlButtons
import com.asu1.quizzer.util.musics.HomeUiEvents

@Composable
fun MusicPlayerActionBar(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    iconSize: Dp = 32.dp,
    updatePlayer: (HomeUiEvents) -> Unit,
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { updatePlayer(HomeUiEvents.SeekToPrevious) }
        ) {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = Icons.Default.FastRewind,
                contentDescription = "Rewind"
            )
        }
        IconButton(
            onClick = { updatePlayer(HomeUiEvents.PlayPause) }
        ) {
            Crossfade(targetState = isPlaying) { playing ->
                Icon(
                    modifier = Modifier.size(iconSize * 1.5f),
                    imageVector = if (playing) Icons.Default.PauseCircle else Icons.Default.PlayCircleFilled,
                    contentDescription = "Play/Pause"
                )
            }
        }
        IconButton(
            onClick = { updatePlayer(HomeUiEvents.SeekToNext) }
        ) {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = Icons.Default.FastForward,
                contentDescription = "Next"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicPlayerActionBarPreview() {
    MusicPlayerActionBar(
        modifier = Modifier.fillMaxWidth().heightIn(50.dp),
        isPlaying = false,
        updatePlayer = {}
    )
}