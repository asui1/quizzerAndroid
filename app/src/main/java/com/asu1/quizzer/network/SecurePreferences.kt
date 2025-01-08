package com.asu1.quizzer.network

import com.asu1.quizzer.BuildConfig

object SecurePreferences {
        const val USERNAME = "asu1"
        val PASSWORD: String = BuildConfig.PASSWORD
        val GOOGLE_CLIENT_ID: String = BuildConfig.GOOGLE_CLIENT_ID
}