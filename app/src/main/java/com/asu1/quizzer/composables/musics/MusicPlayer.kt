package com.asu1.quizzer.composables.musics

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.asu1.quizzer.musics.Music
import com.asu1.quizzer.musics.MusicAllInOne
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.musics.HomeUiEvents
import com.asu1.quizzer.viewModels.MusicListViewModel

@OptIn(UnstableApi::class)
@Composable
fun MusicPlayer(
    modifier: Modifier = Modifier
) {
    val musicListViewModel: MusicListViewModel = hiltViewModel()

    val duration = musicListViewModel.duration
    val progress by musicListViewModel.progress.observeAsState(0f)
    val isPlaying = musicListViewModel.isMusicPlaying
    val currentMusic = musicListViewModel.currentSelectedMusic

    MusicPlayerBody(
        currentMusic = currentMusic,
        isPlaying = isPlaying,
        duration = duration,
        progress = progress,
        updatePlayer = { homeUiEvents ->
            musicListViewModel.onHomeUiEvents(homeUiEvents)
        },
        modifier = modifier.padding(horizontal = 16.dp),
    )
}

@Composable
fun MusicPlayerBody(
    currentMusic: MusicAllInOne,
    isPlaying: Boolean,
    duration: Long,
    progress: Float,
    modifier: Modifier = Modifier,
    updatePlayer: (HomeUiEvents) -> Unit,
){
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (musicInfo, musicProgress, musicPlayerActionBar) = createRefs()

        MusicInfo(
            modifier = Modifier.constrainAs(musicInfo) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            currentMusic = currentMusic
        )
        MusicProgress(
            progress = progress,
            duration = duration,
            updatePlayer = updatePlayer,
            modifier = Modifier.constrainAs(musicProgress) {
                top.linkTo(musicInfo.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        MusicPlayerActionBar(
            modifier = Modifier.constrainAs(musicPlayerActionBar) {
                top.linkTo(musicProgress.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            isPlaying = isPlaying,
            updatePlayer = updatePlayer
        )
    }
}






@Preview(showBackground = true)
@Composable
fun MusicPlayerPreview() {
    MusicPlayerBody(
        currentMusic = MusicAllInOne(
            music = Music(
                title = "Title",
                artist = "Artist",
            ),
            moods = setOf("Mood1", "Mood2"),
        ),
        isPlaying = true,
        duration = 10000,
        progress = 1000f,
        updatePlayer = {},
    )
}