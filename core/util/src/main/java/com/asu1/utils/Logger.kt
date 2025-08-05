@file:Suppress("unused")

package com.asu1.utils

import android.util.Log
import androidx.navigation.NavController
import java.util.logging.Logger

object Logger {
    private const val TAG = "quizzer"
    private var isDebug = false

    fun init(debugMode: Boolean) {
        isDebug = debugMode
    }

    fun debug(input: Any) {
        if (isDebug) {
            Logger.getLogger(TAG).warning(input.toString())
        }
    }

    fun debug(tag1: String, input: Any){
        if (isDebug) {
            Logger.getLogger(TAG + tag1).warning(input.toString())
        }
    }

    fun error(tag1: String, input: Any){
        if (isDebug) {
            Logger.getLogger(TAG + tag1).warning(input.toString())
        }
    }

    fun printBackStack(navController: NavController) {
        if (!isDebug) return

        try {
            // grab the internal backQueue list
            val backQueueField = NavController::class.java
                .getDeclaredField("backQueue")
                .apply { isAccessible = true }

            val backQueue = (backQueueField.get(navController) as? List<*>)
                ?: error("backQueue was not a List")

            for (entry in backQueue) {
                if (entry == null) continue

                // pull out the “destination” field
                val destField = entry::class.java
                    .getDeclaredField("destination")
                    .apply { isAccessible = true }
                val destination = destField.get(entry)

                // now pull the “route” string
                val routeField = destination!!::class.java
                    .getDeclaredField("route")
                    .apply { isAccessible = true }
                val route = routeField.get(destination) as? String
                    ?: "<no-route>"

                debug("Destination: $route")
            }

        } catch (e: NoSuchFieldException) {
            debug("Reflection error: missing field – ${e.message}")
        } catch (e: IllegalAccessException) {
            debug("Reflection error: cannot access field – ${e.message}")
        } catch (e: ClassCastException) {
            debug("Type error reading back stack – ${e.message}")
        } catch (e: IllegalStateException) {
            debug("Unexpected state – ${e.message}")
        }
    }


    fun debugFull(input: String) {
        if (isDebug) {
            val maxLogSize = 1000
            for (i in 0..input.length / maxLogSize) {
                val start = i * maxLogSize
                val end = if ((i + 1) * maxLogSize > input.length) input.length else (i + 1) * maxLogSize
                Log.d(TAG, input.substring(start, end))
            }
        }
    }
}
