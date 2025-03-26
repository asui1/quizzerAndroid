package com.asu1.quiz.viewer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.asu1.quiz.ui.TextStyleManager
import com.asu1.resources.TextStyles
import com.asu1.utils.Logger
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Suppress("REDUNDANT_ELSE_IN_WHEN")
@Composable
fun BuildBody(
    quizBody: BodyType,
    quizStyleManager: TextStyleManager,
){
    when(quizBody){
        is BodyType.TEXT -> quizStyleManager.GetTextComposable(TextStyles.BODY, quizBody.bodyText, modifier = Modifier.fillMaxWidth())
        is BodyType.IMAGE -> {
            val image = remember(quizBody.bodyImage){
                quizBody.bodyImage.asImageBitmap().apply {
                    prepareToDraw()
                }
            }
            Image(
                bitmap = image,
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        is BodyType.YOUTUBE -> {
            val context = LocalContext.current
            AndroidView(factory = {
                val youTubePlayerView = YouTubePlayerView(context)
                youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(quizBody.youtubeId, quizBody.youtubeStartTime.toFloat())
                    }
                })
                youTubePlayerView
            })
        }
        is BodyType.CODE -> {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    .fillMaxWidth()
            ) {
                KotlinCodeHighlighter(
                    code = quizBody.code
                )
            }
        }
        is BodyType.NONE ->{       }
        else -> {
            Logger.debug("Quiz Body Builder", "NOT IMPLEMENTED UI FOR BODY TYPE, ${quizBody.value}")
        }
    }
}