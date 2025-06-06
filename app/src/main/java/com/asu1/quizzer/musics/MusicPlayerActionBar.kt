package com.asu1.quizzer.musics

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MusicPlayerActionBar(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    iconSize: Dp = 32.dp,
    intervalMillis: Long = 200L,
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
                imageVector = Icons.Default.SkipPrevious,
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
                imageVector = Icons.Default.SkipNext,
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