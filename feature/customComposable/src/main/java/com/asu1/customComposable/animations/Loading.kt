package com.asu1.customComposable.animations

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    withText: () -> Int = { R.string.empty_string }
) {
    val source by rememberDotLottieSource()

    Column(
        modifier = modifier
            .pointerInput(Unit) { },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        source
        source?.let { LottieLoader(it, size) }
        LoadingText(textRes = withText(), modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
private fun rememberDotLottieSource(): MutableState<DotLottieSource?> {
    val assetPath = if (isSystemInDarkTheme()) "load_dark.lottie" else "load_light.lottie"
    val sourceState = remember { mutableStateOf<DotLottieSource?>(null) }

    LaunchedEffect(assetPath) {
        sourceState.value = withContext(Dispatchers.IO) {
            DotLottieSource.Asset(assetPath)
        }
    }

    return sourceState
}

@Composable
private fun LottieLoader(
    source: DotLottieSource,
    size: Dp,
    modifier: Modifier = Modifier
) {
    DotLottieAnimation(
        source = source,
        autoplay = true,
        loop = true,
        speed = 1f,
        useFrameInterpolation = false,
        playMode = Mode.FORWARD,
        modifier = modifier
            .background(Color.Transparent)
            .size(size)
    )
}

@Composable
private fun LoadingText(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(textRes),
        style = QuizzerTypographyDefaults.quizzerLabelMediumMedium,
        modifier = modifier
    )
}
