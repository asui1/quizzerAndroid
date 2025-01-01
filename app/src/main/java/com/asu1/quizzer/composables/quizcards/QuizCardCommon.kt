package com.asu1.quizzer.composables.quizcards

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.skydoves.landscapist.glide.GlideImage
import java.io.ByteArrayOutputStream

fun loadImageAsByteArray(context: Context, resId: Int): ByteArray {
    val bitmap = BitmapFactory.decodeResource(context.resources, resId)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsView(tags: List<String>, modifier: Modifier = Modifier, maxLines: Int = 2) {
    ContextualFlowRow(
        modifier = modifier,
        maxLines = maxLines,
        itemCount = 1,
    ) {
        tags.forEach { tag ->
            Text(text = tag,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun QuizImage(uuid: String, title: String, modifier: Modifier = Modifier){
    val imageUrl = "https://quizzer.co.kr/api/images/${uuid}.png"

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