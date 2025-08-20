package com.asu1.mainpage.screens.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.UserBackground1
import com.asu1.resources.UserBackground2
import com.asu1.resources.UserBackground3
import com.asu1.resources.UserBackground4
import com.asu1.resources.UserBackground5
import com.asu1.resources.UserBackground6
import com.asu1.resources.UserBackground7
import com.asu1.resources.UserBackground8
import kotlin.random.Random

@Composable
fun UriImageButton(modifier: Modifier = Modifier, urlToImage: String?, nickname: Char = 'Q') {
    val painter = rememberAsyncImagePainter(
        model = urlToImage,
    )
    val isError = painter.state is AsyncImagePainter.State.Error
    val backgroundColor = remember{
        val randomInt = Random.nextInt(0, 8)
        when(randomInt){
            0 -> UserBackground1
            1 -> UserBackground2
            2 -> UserBackground3
            3 -> UserBackground4
            4 -> UserBackground5
            5 -> UserBackground6
            6 -> UserBackground7
            7 -> UserBackground8
            else -> UserBackground1
        }
    }

    Box(modifier = modifier,
        contentAlignment = Alignment.Center) {
        if (isError) {
            BoxWithTextAndColorBackground(modifier = Modifier, backgroundColor, nickname)
        } else {
            Image(
                painter = painter,
                contentDescription = "User Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUriImageButton(){
    QuizzerAndroidTheme {
        UriImageButton(modifier = Modifier, urlToImage = null, nickname = 'Q')
    }
}
