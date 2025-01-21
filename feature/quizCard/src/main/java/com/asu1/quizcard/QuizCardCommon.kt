package com.asu1.quizcard

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
// TODO: At runtime no static method ContextualFlowRow error...
//    ContextualFlowRow(
//        modifier = modifier,
//        maxLines = maxLines,
//        itemCount = 1,
//    ) {
//        tags.forEach { tag ->
//            Text(text = tag,
//                color = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.padding(end = 4.dp),
//                style = MaterialTheme.typography.bodySmall,
//                overflow = TextOverflow.Ellipsis,
//            )
//        }
//    }
}

@Preview
@Composable
fun PreviewTagsView() {
    TagsView(tags = listOf("Tag 1", "Tag 2", "Tag 3"))
}

