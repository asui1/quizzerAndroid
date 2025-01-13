package com.asu1.quizzer.composables.musics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.musics.MusicAllInOne

@Composable
fun MusicInfo(
    modifier: Modifier = Modifier,
    currentMusic: MusicAllInOne,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = currentMusic.music.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = currentMusic.music.artist,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MusicInfoPreview() {
    MusicInfo(
        currentMusic = MusicAllInOne(
            music = com.asu1.quizzer.musics.Music(
                title = "Title",
                artist = "Artist",
            ),
            moods = setOf("Mood1", "Mood2"),
        ),
        modifier = Modifier.fillMaxWidth()
    )
}