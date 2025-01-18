@file:Suppress("DEPRECATION")

package com.asu1.quizzer.util

import android.app.Activity
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun setTopBarColor(view: View, color: Color){

    // CHECK: statusBarColor DEPRECATED (no more use in api 35)
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        window.statusBarColor = color.toArgb()
    }
}

fun setBottomBarColor(view: View, color: Color){
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        window.navigationBarColor = color.toArgb()
    }
}