package com.asu1.quizzer.util

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

class Logger {
    private val tag = "quizzer"
    private val isDebug = true

    fun debug(input: String) {
        if (isDebug) {
            Log.d(tag, input)
        }
    }
    fun printBackStack(navController: NavController) {
        try {
            val backQueueField = NavController::class.java.getDeclaredField("backQueue")
            backQueueField.isAccessible = true
            val backQueue = backQueueField.get(navController) as List<*>
            for (entry in backQueue) {
                val destinationField = entry!!::class.java.getDeclaredField("destination")
                destinationField.isAccessible = true
                val destination = destinationField.get(entry)
                val routeField = destination!!::class.java.getDeclaredField("route")
                routeField.isAccessible = true
                val route = routeField.get(destination) as String
                debug("Destination: $route")
            }
        } catch (e: Exception) {
            debug("Error accessing back stack: ${e.message}")
        }
    }
}



fun Dp.times(factor: Float): Dp = (this.value * factor).dp