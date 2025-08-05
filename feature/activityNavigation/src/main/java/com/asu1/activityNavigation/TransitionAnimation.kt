package com.asu1.activityNavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

const val TRANSITION_DURATION = 300
fun enterFadeInTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) {
    return {
        fadeIn(
            animationSpec = tween(
                TRANSITION_DURATION, easing = LinearEasing
            )
        ) + slideIntoContainer(
            animationSpec = tween(TRANSITION_DURATION, easing = EaseIn),
            towards = AnimatedContentTransitionScope.SlideDirection.Start
        )
    }
}

fun exitFadeOutTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) {
    return {
        fadeOut(
            animationSpec = tween(
                TRANSITION_DURATION, easing = LinearEasing
            )
        ) + slideOutOfContainer(
            animationSpec = tween(TRANSITION_DURATION, easing = EaseOut),
            towards = AnimatedContentTransitionScope.SlideDirection.End
        ) }
}

fun enterFromRightTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) {
    return { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(TRANSITION_DURATION)) }
}

fun exitToRightTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) {
    return { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(TRANSITION_DURATION)) }
}
