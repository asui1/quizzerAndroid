package com.asu1.quizzer.util

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object NavMultiClickPreventer {
    private val isNavigated = mutableStateOf(false)
    private val scope = CoroutineScope(Dispatchers.Main)

    fun navigate(navController: NavController, targetRoute: Route, builder: NavOptionsBuilder.() -> Unit = {}) {
        if (!isNavigated.value) {
            isNavigated.value = true
            navController.navigate(targetRoute){
                builder()
            }
            resetAfterDelay()
        }
    }

    private fun resetAfterDelay() {
        scope.launch {
            delay(500)
            isNavigated.value = false
        }
    }
}

