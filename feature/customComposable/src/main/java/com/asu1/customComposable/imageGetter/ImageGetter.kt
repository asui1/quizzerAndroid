package com.asu1.customComposable.imageGetter

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.asu1.customComposable.animations.LoadingAnimation
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
import com.asu1.utils.images.createEmptyBitmap
import com.asu1.utils.uriToByteArray
import kotlinx.coroutines.launch

@Composable
fun ImageGetter(
    modifier: Modifier = Modifier,
    image: Bitmap,
    onImageUpdate: (Bitmap) -> Unit,
    width: Dp? = null,
    height: Dp? = null,
    topBar: @Composable (Modifier) -> Unit = {}
) {
    // 1) encapsulate all the remember/launchedEffect/launcher logic
    val state = rememberImageGetterState(width, height, onImageUpdate)

    // 2) just delegate to an animated container
    AnimatedContent(
        targetState = state.isLoading.value,
        transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(500)) }
    ) { loading ->
        if (loading) {
            LoadingView(modifier.size(state.width, state.height), state.size)
        } else {
            ImageDisplayView(
                modifier = modifier.size(state.width, state.height),
                image = image,
                onImageUpdate = onImageUpdate,
                topBar = topBar,
                state = state
            )
        }
    }
}

@Stable
data class ImageGetterState(
    val width: Dp,
    val height: Dp,
    val size: Dp,
    val isLoading: MutableState<Boolean>,
    val launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
)

@Composable
private fun rememberImageGetterState(
    width: Dp?,
    height: Dp?,
    onImageUpdate: (Bitmap) -> Unit
): ImageGetterState {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val w = width  ?: 200.dp
    val h = height ?: 200.dp
    val size = min(w, h)

    val isLoadingState = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            isLoadingState.value = true
            scope.launch {
                uriToByteArray(ctx, uri, w, h)?.let(onImageUpdate)
                isLoadingState.value = false
            }
        }
    }

    // 3) bundle into ImageGetterState
    return remember(w, h) {
        ImageGetterState(
            width     = w,
            height    = h,
            size      = size,
            isLoading = isLoadingState,
            launcher  = launcher
        )
    }
}

@Composable
private fun LoadingView(modifier: Modifier, size: Dp) {
    LoadingAnimation(
        modifier = modifier,
        size = size
    )
}

@Composable
private fun ImageDisplayView(
    modifier: Modifier,
    image: Bitmap,
    onImageUpdate: (Bitmap) -> Unit,
    topBar: @Composable (Modifier) -> Unit,
    state: ImageGetterState
) {
    Column(
        modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
    ) {
        topBar(Modifier.fillMaxWidth())
        Box(
            Modifier
                .fillMaxSize()
                .clickable {
                    state.launcher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly)
                    ) },
            contentAlignment = Alignment.Center
        ) {
            if (image.width < 2) {
                ImagePlaceholder(state.size)
            } else {
                DisplayedImage(image)
                DeleteIcon(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) { onImageUpdate(createEmptyBitmap()) }
            }
        }
    }
}

@Composable
private fun ImagePlaceholder(size: Dp) {
    Icon(Icons.Default.ImageSearch, contentDescription = null, Modifier.size(size / 2))
    Text(stringResource(R.string.add_image), textAlign = TextAlign.Center)
}

@Composable
private fun DisplayedImage(bitmap: Bitmap) {
    Image(
        bitmap = remember(bitmap) { bitmap.asImageBitmap().apply { prepareToDraw() } },
        contentDescription = stringResource(R.string.selected_image),
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun DeleteIcon(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Icon(
        Icons.Default.RemoveCircle,
        contentDescription = stringResource(R.string.delete_image),
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ImageGetterPreview() {
    QuizzerAndroidTheme {
        ImageGetter(
            topBar = { modifier ->
            },
            image = createEmptyBitmap(),
            onImageUpdate = {}
        )
    }
}
