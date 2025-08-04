package com.asu1.imagecolor

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class EffectGraphicsInfo(
    val progress: Float = 0f,
    val translationY: Float = 0f,
    val translationX: Float = 0f,
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
)

fun Modifier.setEffectGraphicsLayer(
    effectGraphicsInfo: EffectGraphicsInfo
): Modifier{
    return this.then(Modifier.graphicsLayer {
//        alpha = 0.8f
        translationY = size.height * effectGraphicsInfo.translationY
        translationX = size.width * effectGraphicsInfo.translationX
        scaleX = effectGraphicsInfo.scaleX
        scaleY = effectGraphicsInfo.scaleY
    })
}
