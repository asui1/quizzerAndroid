package com.asu1.customComposable.animations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource

@Composable
fun UploadingAnimation(modifier: Modifier = Modifier, size: Dp = 200.dp) {
    Box(
        modifier = modifier
            .pointerInput(Unit) { }
            .zIndex(2f)
            .fillMaxSize(),
    ) {
        DotLottieAnimation(
            source = DotLottieSource.Asset("upload.lottie"),
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
