package com.asu1.quizzer.composables.effects

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.asu1.imagecolor.EffectGraphicsInfo
import com.asu1.imagecolor.setEffectGraphicsLayer

@Composable
fun EffectBuilder(
    modifier: Modifier = Modifier,
    resourceUrl: String,
    color: Color,
    blendModeCompat: BlendModeCompat = BlendModeCompat.COLOR,
    contentScale: ContentScale = ContentScale.Fit,
    effectGraphicsInfos: List<EffectGraphicsInfo>,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url(
            resourceUrl
        )
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    key(color, resourceUrl){
        val dynamicProperties = rememberLottieDynamicProperties(
            rememberLottieDynamicProperty(
                property = LottieProperty.COLOR_FILTER,
                value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    color.toArgb(),
                    blendModeCompat
                ),
                keyPath = arrayOf(
                    "**"
                )
            )
        )
        for(effectGraphicsInfo in effectGraphicsInfos){
            LottieAnimation(
                contentScale = contentScale,
                composition = composition,
                progress = { (progress + effectGraphicsInfo.progress)%1f },
                dynamicProperties = dynamicProperties,
                modifier = modifier.fillMaxSize()
                    .setEffectGraphicsLayer(effectGraphicsInfo)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EffectBuilderPreview() {
    val currentItem = com.asu1.imagecolor.Effect.BUBBLES
    EffectBuilder(
        color = Color.Red,
        resourceUrl = "https://lottie.host/fdcf2044-027a-4199-9762-6b3d214d8b69/duRyaaTkuY.lottie",
        blendModeCompat = currentItem.blendmode,
        contentScale = currentItem.contentScale,
        effectGraphicsInfos = listOf(
            EffectGraphicsInfo(
                progress = 0f,
            ),
        )
    )
}