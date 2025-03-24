package com.asu1.customComposable.effects

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState

@Composable
fun produceDrawLoopCounter(speed: Float = 1f): State<Float> {
    return produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f * speed
            }
        }
    }
}