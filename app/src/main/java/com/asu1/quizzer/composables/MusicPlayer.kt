package com.asu1.quizzer.composables

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.focusable
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.asu1.quizzer.viewModels.MusicListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import com.asu1.quizzer.musics.Music
import com.asu1.quizzer.musics.MusicAllInOne
import com.asu1.quizzer.util.musics.ControlButtons

@OptIn(UnstableApi::class)
@Composable
fun MusicPlayer(
) {
    val musicListViewModel: MusicListViewModel = hiltViewModel()
    val context = LocalContext.current
    LaunchedEffect(Unit){
        musicListViewModel.preparePlayer(context)
    }

    TextButton(
        onClick = {
            musicListViewModel.updatePlaylist(
                ControlButtons.PlayPause
            )
        }
    ) {
        Text("Play")
    }
}

@Preview(showBackground = true)
@Composable
fun MusicPlayerPreview() {
    MusicPlayer()
}