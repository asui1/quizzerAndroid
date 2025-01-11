package com.asu1.quizzer.composables

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.asu1.quizzer.musics.Music
import com.asu1.quizzer.musics.MusicAllInOne
import com.asu1.quizzer.util.musics.ControlButtons
import com.asu1.quizzer.viewModels.MusicListViewModel

@OptIn(UnstableApi::class)
@Composable
fun MusicPlayer(
    modifier: Modifier = Modifier
) {
    val musicListViewModel: MusicListViewModel = hiltViewModel()
    val currentPlayingIndex by musicListViewModel.currentPlayingIndex.collectAsStateWithLifecycle()
    val totalDurationInMS by musicListViewModel.totalDurationInMS.collectAsStateWithLifecycle()
    val currentDurationInMS by musicListViewModel.currentDurationInMs.collectAsStateWithLifecycle()
    val isPlaying by musicListViewModel.isPlaying.collectAsStateWithLifecycle()
    val playlist by musicListViewModel.playlist.collectAsStateWithLifecycle()
    MusicPlayerBody(
        currentMusic = playlist[currentPlayingIndex],
        isPlaying = isPlaying,
        currentDurationInMS = currentDurationInMS,
        totalDurationInMS = totalDurationInMS,
        updatePlayer = { controlButtons ->
            musicListViewModel.updatePlayer(controlButtons)
        },
        modifier = modifier,
    )
}

@Composable
fun MusicPlayerBody(
    currentMusic: MusicAllInOne,
    isPlaying: Boolean,
    currentDurationInMS: Long,
    totalDurationInMS: Long,
    modifier: Modifier = Modifier,
    updatePlayer: (ControlButtons) -> Unit,
){
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (
            playingDrawable,
            actionBar,
            musicInfo
        ) = createRefs()

    }
}

@Preview(showBackground = true)
@Composable
fun MusicPlayerPreview() {
    MusicPlayerBody(
        currentMusic = MusicAllInOne(
            music = Music("sample1", "test1"),
            moods = setOf("Happy")
        ),
        isPlaying = false,
        currentDurationInMS = 50000,
        totalDurationInMS = 210000,
        updatePlayer = {}
    )
}