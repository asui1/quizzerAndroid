package com.asu1.quizzer.composables.animations

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource

@Composable
fun LoadingAnimation(modifier: Modifier = Modifier, size: Dp = 200.dp) {
    val assetPath = if(isSystemInDarkTheme()) "load_dark.lottie" else "load_light.lottie"
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DotLottieAnimation(
            source = DotLottieSource.Asset(assetPath),
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
