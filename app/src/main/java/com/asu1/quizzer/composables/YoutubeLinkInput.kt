package com.asu1.quizzer.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.asu1.resources.R
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
                label = { Text(stringResource(R.string.enter_youtube_link)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("QuizCreatorBodyYoutubeLinkTextField"),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val (id, time) = parseYoutubeLink(link)
                        onYoutubeUpdate(id, time)
                    }
                ),
                placeholder = {
                    if (clipboardText.isNotEmpty()) {
                        Text(clipboardText)
                    }
                }
            )
            TextButton(
                onClick = {
                    val (id, time) = parseYoutubeLink(link)
                    if(id == "") {
                        Toast.makeText(context,
                            context.getString(R.string.invalid_youtube_link), Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    onYoutubeUpdate(id, time)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.load_video))
            }
        }
    } else {
        Row(modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(
                onClick = {
                    onYoutubeUpdate("DELETE", 0)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveCircleOutline,
                    contentDescription = stringResource(R.string.delete_current_quiz_s_youtube)
                )
            }
        }
        AndroidView(factory = {
            val youTubePlayerView = YouTubePlayerView(context)
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(youtubeId, startTime.toFloat())
                }
            })
            youTubePlayerView
        }, modifier = Modifier.fillMaxSize())
    }
}

//https://www.youtube.com/live/AkvX-E9lnBs?si=NM78tAR8WUL3jB7O&t=8236
fun parseYoutubeLink(link: String): Pair<String, Int> {

    val regex =
        if(link.contains("youtube.com/watch?v=")){
            Regex("v=([a-zA-Z0-9_-]+)(?:.*?[?&]t=(\\d+))?")
        }
        else if(link.contains("youtu.be")){
            Regex("youtu\\.be/([a-zA-Z0-9_-]+)(?:.*?[?&]t=(\\d+))?")
        }
        else if(link.contains("youtube.com/live")){
            Regex("youtube.com/live/([a-zA-Z0-9_-]+)(?:.*?[?&]t=(\\d+))?")
        }
        else{
            return "" to 0
        }
    val matchResult = regex.find(link)
    val id = matchResult?.groups?.get(1)?.value ?: ""
    val time = matchResult?.groups?.get(2)?.value?.toInt() ?: 0
    return id to time
}

@Preview(showBackground = true)
@Composable
fun YoutubeLinkInputPreview() {
    com.asu1.resources.QuizzerAndroidTheme {
        YoutubeLinkInput(
            youtubeId = "jfGCOAwlPTE",
            startTime = 8072,
            onYoutubeUpdate = { _, _ -> }
        )
    }
}

