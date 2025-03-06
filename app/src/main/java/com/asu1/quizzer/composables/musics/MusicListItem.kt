package com.asu1.quizzer.composables.musics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.asu1.appdata.music.MusicAllInOne
import com.asu1.quizzer.util.constants.sampleMusicList

@Composable
fun MusicListItem(
    musicAllInOne: MusicAllInOne,
    modifier: Modifier = Modifier,
    playingIcon: @Composable (Modifier) -> Unit,
    handle: @Composable (Modifier) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
    ){
        val (handleIcon, title, artist, playIcon) = createRefs()
        Text(
            text = musicAllInOne.music.title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(artist.top)
            }
        )
        Text(
            text = musicAllInOne.music.artist,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier.constrainAs(artist) {
                start.linkTo(parent.start)
                top.linkTo(title.bottom)
                bottom.linkTo(parent.bottom)
            }
        )
        playingIcon(
            Modifier
                .size(MaterialTheme.typography.labelSmall.fontSize.value.dp)
                .constrainAs(playIcon) {
                start.linkTo(artist.end, margin = 8.dp)
                bottom.linkTo(parent.bottom)
                top.linkTo(title.bottom)
            }
        )
        handle(
            Modifier.constrainAs(handleIcon) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewMusicListItem() {
    MusicListItem(
        musicAllInOne = sampleMusicList[0],
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        playingIcon = {modifier ->
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                modifier = modifier
            )
        },
        handle = {modifier ->
            Icon(
                imageVector = Icons.Default.PlayCircleFilled,
                contentDescription = "Play",
                modifier = modifier
            )
        }
    )
}