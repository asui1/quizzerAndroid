package com.asu1.quizzer

import androidx.navigation.NavController
import com.asu1.activityNavigation.Route

fun hasVisitedRoute(
    navController: NavController,
    route: Route
): Boolean {
    val previousEntry = navController.previousBackStackEntry
    return previousEntry?.destination?.route == route::class.qualifiedName
}

