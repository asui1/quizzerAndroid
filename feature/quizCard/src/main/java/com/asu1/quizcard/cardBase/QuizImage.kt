package com.asu1.quizcard.cardBase

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.asu1.resources.BASE_URL_API
import com.asu1.resources.R
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun QuizImage(uuid: String, title: String, modifier: Modifier = Modifier){
    val imageUrl = "${BASE_URL_API}images/${uuid}.png"

    Box(modifier = modifier) {
        if (uuid.length == 1) {
            Image(
                painter = painterResource(id = R.drawable.question2),
                contentDescription = "Image for quiz $title",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            GlideImage(
                imageModel = {imageUrl},
                modifier = Modifier.fillMaxSize(),
                failure = {
                    Image(
                        painter = painterResource(id = R.drawable.question2),
                        contentDescription = "Image for quiz $title",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                },
            )
        }
    }
}