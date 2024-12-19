package com.asu1.quizzer.util

import android.util.Log
import androidx.navigation.NavController
import com.asu1.quizzer.BuildConfig
import java.util.logging.Logger

object Logger {
    private val tag = "quizzer"

    fun debug(input: Any) {
            Logger.getLogger(tag).warning(input.toString())
    }

    fun debug(tag1: String, input: Any){
        if (BuildConfig.isDebug) {
            Logger.getLogger(tag + tag1).warning(input.toString())
        }
    }

    fun printBackStack(navController: NavController) {
        if (BuildConfig.isDebug) {
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

    fun debugFull(input: String) {
        if (BuildConfig.isDebug) {
            val maxLogSize = 1000
            for (i in 0..input.length / maxLogSize) {
                val start = i * maxLogSize
                val end = if ((i + 1) * maxLogSize > input.length) input.length else (i + 1) * maxLogSize
                Log.d(tag, input.substring(start, end))
            }
        }
    }
}