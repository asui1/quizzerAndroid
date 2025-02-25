package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.asu1.models.serializers.BodyType
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.resources.TextStyles
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun BuildBody(
    quizBody: BodyType,
    quizStyleManager: TextStyleManager,
){
    val context = LocalContext.current

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
        else -> {}
    }
}