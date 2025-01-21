package com.asu1.quizzer.composables.animations

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.asu1.resources.R
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LoadingAnimation(modifier: Modifier = Modifier, size: Dp = 200.dp, withText: () -> Int = { R.string.empty_string }) {
    val assetPath = if (isSystemInDarkTheme()) "load_dark.lottie" else "load_light.lottie"
    var lottieSource by remember { mutableStateOf<DotLottieSource?>(null) }

    LaunchedEffect(assetPath) {
        lottieSource = withContext(Dispatchers.IO) {
            DotLottieSource.Asset(assetPath)
        }
    }

    Column(
        modifier = modifier
            .pointerInput(Unit) { }
            .zIndex(2f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
        Text(
            text = stringResource(withText()),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}
