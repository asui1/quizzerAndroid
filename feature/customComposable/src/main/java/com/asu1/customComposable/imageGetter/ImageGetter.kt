package com.asu1.customComposable.imageGetter

import android.graphics.Bitmap
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
import com.asu1.utils.images.createEmptyBitmap
import com.asu1.utils.launchPhotoPicker
import com.asu1.utils.uriToByteArray
import kotlinx.coroutines.launch

@Composable
fun ImageGetter(
    modifier: Modifier = Modifier,
    image: Bitmap,
    onImageUpdate: (Bitmap) -> Unit,
    width: Dp? = null, height: Dp? = null,
    topBar: @Composable (Modifier) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val photoPickerLauncher = launchPhotoPicker() { uri ->
        isLoading = true
        scope.launch {
            val imageBitmap = uriToByteArray(context, uri, width, height)
            if (imageBitmap != null) {
                onImageUpdate(imageBitmap)
            }
            isLoading = false
        }
    }

    val localWidth = width ?: 200.dp
    val localHeight = height ?: 200.dp
    val size = min(localWidth, localHeight)
    AnimatedContent(
        targetState = isLoading,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        label = "Design Scorecard",
    ) { targetState ->
        when(targetState){
            true ->{
                LoadingAnimation(
                    modifier = modifier
                        .width(localWidth)
                        .height(localHeight),
                    size = size,
                )
            }
            false -> {
                Column(
                    modifier = modifier.width(localWidth)
                ){
                    topBar(Modifier.fillMaxWidth())
                    Box(
                        modifier = Modifier
                            .width(localWidth)
                            .height(localHeight)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("ImageGetterBoxGetImage"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (image.width <= 1 || image.height <= 1) {
                            Icon(
                                imageVector = Icons.Default.ImageSearch,
                                contentDescription = stringResource(R.string.add_image),
                                modifier = Modifier.size(size/2)
                            )
                            Text(
                                stringResource(R.string.add_image),
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        } else {
                            val imageBitmap = remember(image) {
                                image.asImageBitmap().apply {
                                    prepareToDraw()
                                }
                            }
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = stringResource(R.string.selected_image),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Icon(
                                imageVector = Icons.Default.RemoveCircle,
                                contentDescription = stringResource(R.string.delete_image),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .clickable {
                                        onImageUpdate(createEmptyBitmap())
                                    }
                                    .padding(top = 4.dp, end = 4.dp)
                            )
                        }
                    }
                }

            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ImageGetterPreview() {
    QuizzerAndroidTheme {
        ImageGetter(
            topBar = { modifier ->
                Row(
                    modifier = modifier.fillMaxWidth()
                ){
                    Text("Remove Background")
                }
            },
            image = createEmptyBitmap(),
            onImageUpdate = {}
        )
    }

}