package com.asu1.quizzer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    // Application-level setup
    override fun onCreate() {
        super.onCreate()

    }
}