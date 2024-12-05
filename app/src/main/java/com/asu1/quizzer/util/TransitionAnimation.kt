package com.asu1.quizzer.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

const val durations = 300
fun enterFadeInTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> androidx.compose.animation.EnterTransition?) {
    return {
        fadeIn(
            animationSpec = tween(
                durations, easing = LinearEasing
            )
        ) + slideIntoContainer(
            animationSpec = tween(durations, easing = EaseIn),
            towards = AnimatedContentTransitionScope.SlideDirection.Start
        )
    }
}

fun exitFadeOutTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> androidx.compose.animation.ExitTransition?) {
    return {
        fadeOut(
            animationSpec = tween(
                durations, easing = LinearEasing
            )
        ) + slideOutOfContainer(
            animationSpec = tween(durations, easing = EaseOut),
            towards = AnimatedContentTransitionScope.SlideDirection.End
        ) }
}

fun enterFromRightTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> androidx.compose.animation.EnterTransition?) {
    return { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(durations)) }
}

fun exitToRightTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> androidx.compose.animation.ExitTransition?) {
    return { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(durations)) }
}