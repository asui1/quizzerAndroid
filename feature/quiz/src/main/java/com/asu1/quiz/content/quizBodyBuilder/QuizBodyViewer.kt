package com.asu1.quiz.content.quizBodyBuilder

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.asu1.models.serializers.BodyType
import com.asu1.quiz.ui.textStyleManager.BodyTextStyle
import com.asu1.utils.Logger
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Suppress("REDUNDANT_ELSE_IN_WHEN")
@Composable
fun QuizBodyViewer(
    quizBody: BodyType
) {
    // Skip if no content
    if (quizBody is BodyType.NONE) return

    // Consistent padding for all body types
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        BodyContentViewer(bodyState = quizBody)
    }
}

@Composable
private fun BodyContentViewer(
    bodyState: BodyType
) {
    when (bodyState) {
        is BodyType.TEXT    -> TextBodyViewer(text = bodyState.bodyText)
        is BodyType.IMAGE   -> ImageBodyViewer(image = bodyState.bodyImage)
        is BodyType.YOUTUBE -> YouTubeBodyViewer(
            id     = bodyState.youtubeId,
            start  = bodyState.youtubeStartTime
        )
        is BodyType.CODE    -> CodeBodyViewer(code = bodyState.code)
        else                -> Logger.debug("QuizBodyViewer", "Unhandled body type: $bodyState")
    }
}

@Composable
private fun TextBodyViewer(
    text: String
) {
    BodyTextStyle.GetTextComposable(
        text = text,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ImageBodyViewer(
    image: Bitmap
) {
    image.asImageBitmap().let { bitmap ->
        val drawn = remember(bitmap) {
            bitmap.apply { prepareToDraw() }
        }
        Image(
            bitmap = drawn,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun YouTubeBodyViewer(
    id: String,
    start: Int
) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            YouTubePlayerView(context).apply {
                addYouTubePlayerListener(
                    object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(id, start.toFloat())
                        }
                    }
                )
            }
        }
    )
}

@Composable
private fun CodeBodyViewer(
    code: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .fillMaxWidth()
    ) {
        KotlinCodeHighlighter(
            code = code,
            modifier = Modifier.padding(12.dp)
        )
    }
}
