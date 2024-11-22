package com.asu1.quizzer.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
val durations = 500
fun enterFadeInTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> androidx.compose.animation.EnterTransition?) {
    return { fadeIn(tween(durations)) }
}

fun exitFadeOutTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> androidx.compose.animation.ExitTransition?) {
    return { fadeOut(tween(durations)) }
}

fun enterFromRightTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> androidx.compose.animation.EnterTransition?) {
    return { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(durations)) }
}

fun exitToRightTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> androidx.compose.animation.ExitTransition?) {
    return { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(durations)) }
}