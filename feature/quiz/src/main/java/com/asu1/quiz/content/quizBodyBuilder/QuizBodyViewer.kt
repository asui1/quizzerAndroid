package com.asu1.quiz.content.quizBodyBuilder

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
    // Skip entirely if there's no body
    if (quizBody is BodyType.NONE) return

    // Wrap all non-NONE bodies in consistent padding
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        when (quizBody) {
            is BodyType.TEXT -> {
                BodyTextStyle.GetTextComposable(
                    text = quizBody.bodyText,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is BodyType.IMAGE -> {
                val bitmap = remember(quizBody.bodyImage) {
                    quizBody.bodyImage
                        .asImageBitmap()
                        .apply { prepareToDraw() }
                }
                Image(
                    bitmap = bitmap,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            is BodyType.YOUTUBE -> {
                val context = LocalContext.current
                AndroidView(
                    factory = {
                        YouTubePlayerView(context).apply {
                            addYouTubePlayerListener(
                                object : AbstractYouTubePlayerListener() {
                                    override fun onReady(youTubePlayer: YouTubePlayer) {
                                        youTubePlayer.cueVideo(
                                            quizBody.youtubeId,
                                            quizBody.youtubeStartTime.toFloat()
                                        )
                                    }
                                }
                            )
                        }
                    }
                )
            }

            is BodyType.CODE -> {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .fillMaxWidth()
                ) {
                    KotlinCodeHighlighter(
                        code = quizBody.code,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            else -> {
                Logger.debug("BuildBody", "Unhandled body type: $quizBody")
            }
        }
    }
}