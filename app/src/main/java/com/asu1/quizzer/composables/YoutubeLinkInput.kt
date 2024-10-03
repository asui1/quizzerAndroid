package com.asu1.quizzer.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView



@Composable
fun YoutubeLinkInput(
    youtubeId: String,
    startTime: Int,
    onYoutubeUpdate: (String, Int) -> Unit,
) {
    val context = LocalContext.current
    var link by remember { mutableStateOf("") }

    val clipboardManager = LocalClipboardManager.current
    val clipboardText = remember { clipboardManager.getText()?.text ?: "" }

    if (youtubeId.isEmpty()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = link,
                onValueChange = { link = it },
                label = { Text("Enter YouTube Link") },
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val (id, time) = parseYoutubeLink(link)
                        onYoutubeUpdate(id, time)
                    }
                ),
                placeholder = {
                    if (clipboardText.isNotEmpty()) {
                        Text("Suggested: $clipboardText")
                    }
                }
            )
            Button(
                onClick = {
                    val (id, time) = parseYoutubeLink(link)
                    onYoutubeUpdate(id, time)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Load Video")
            }
        }
    } else {
        AndroidView(factory = {
            val youTubePlayerView = YouTubePlayerView(context)
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(youtubeId, startTime.toFloat())
                }
            })
            youTubePlayerView
        }, modifier = Modifier.fillMaxSize())
    }
}

fun parseYoutubeLink(link: String): Pair<String, Int> {
    val regex = Regex("v=([a-zA-Z0-9_-]+).*t=(\\d+)")
    val matchResult = regex.find(link)
    val id = matchResult?.groups?.get(1)?.value ?: ""
    val time = matchResult?.groups?.get(2)?.value?.toInt() ?: 0
    return id to time
}

@Preview(showBackground = true)
@Composable
fun YoutubeLinkInputPreview() {
    QuizzerAndroidTheme {
        YoutubeLinkInput(
            youtubeId = "jfGCOAwlPTE",
            startTime = 8072,
            onYoutubeUpdate = { _, _ -> }
        )
    }
}

