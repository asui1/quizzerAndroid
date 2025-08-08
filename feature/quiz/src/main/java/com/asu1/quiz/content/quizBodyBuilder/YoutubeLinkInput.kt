package com.asu1.quiz.content.quizBodyBuilder

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircleOutline
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
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@Composable
fun YoutubeLinkInput(
    youtubeId: String,
    startTime: Int,
    onYoutubeUpdate: (String, Int) -> Unit
) {
    // 1) control UI mode: input vs viewer
    if (youtubeId.isEmpty()) {
        YoutubeLinkField(onLoad = { link ->
            val (id, time) = parseYoutubeLink(link)
            onYoutubeUpdate(id, time)
        })
    } else {
        YoutubeLinkViewer(
            youtubeId = youtubeId,
            startTime = startTime,
            onDelete  = { onYoutubeUpdate("", 0) }
        )
    }
}

@Composable
private fun YoutubeLinkField(
    onLoad: (String) -> Unit
) {
    val context          = LocalContext.current
    var linkText by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboard.current
    val clipboardText    = remember { clipboardManager.nativeClipboard.primaryClip.toString() }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value          = linkText,
            onValueChange  = { linkText = it },
            label          = { Text(stringResource(R.string.enter_youtube_link)) },
            placeholder    = { if (clipboardText.isNotEmpty()) Text(clipboardText) },
            modifier       = Modifier
                .fillMaxWidth()
                .testTag("QuizCreatorBodyYoutubeLinkTextField"),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onLoad(linkText) }
            )
        )
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = {
            val (id, _) = parseYoutubeLink(linkText)
            if (id.isEmpty()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.invalid_youtube_link),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                onLoad(linkText)
            }
        }) {
            Text(stringResource(R.string.load_video))
        }
    }
}

@Composable
private fun YoutubeLinkViewer(
    youtubeId: String,
    startTime: Int,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        // delete overlay
        IconButton(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                Icons.Default.RemoveCircleOutline,
                contentDescription = stringResource(R.string.delete_current_quiz_s_youtube)
            )
        }
        // player view
        AndroidView(
            factory = {
                YouTubePlayerView(context).apply {
                    addYouTubePlayerListener(
                        object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                youTubePlayer.cueVideo(youtubeId, startTime.toFloat())
                            }
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )
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
    QuizzerAndroidTheme {
        YoutubeLinkInput(
            youtubeId = "jfGCOAwlPTE",
            startTime = 8072,
            onYoutubeUpdate = { _, _ -> }
        )
    }
}

