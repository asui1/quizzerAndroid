package com.asu1.quizzer.composables.animations

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LoadingAnimation(modifier: Modifier = Modifier, size: Dp = 200.dp) {
    val assetPath = if (isSystemInDarkTheme()) "load_dark.lottie" else "load_light.lottie"
    var lottieSource by remember { mutableStateOf<DotLottieSource?>(null) }

    LaunchedEffect(assetPath) {
        lottieSource = withContext(Dispatchers.IO) {
            DotLottieSource.Asset(assetPath)
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) { }
            .zIndex(2f)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        lottieSource?.let {
            DotLottieAnimation(
                source = it,
                autoplay = true,
                loop = true,
                speed = 1f,
                useFrameInterpolation = false,
                playMode = Mode.FORWARD,
                modifier = Modifier
                    .background(Color.Transparent)
                    .size(size)
            )
        }
    }
}
