package com.asu1.quizzer.util.musics

import java.util.Locale

fun msToTime(durationInMS: Long): String {
    if (durationInMS < 0) {
        throw IllegalArgumentException("Duration must be non-negative")
    }
    val seconds = durationInMS / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format(Locale.US, "%d:%02d", minutes, remainingSeconds)
}

fun floatToTime(progress: Float): String{
    if (progress < 0) {
        throw IllegalArgumentException("Duration must be non-negative")
    }
    val progressInSeconds = progress.toInt()
    val minutes = progressInSeconds / 60
    val remainingSeconds = progressInSeconds % 60
    return String.format(Locale.US, "%d:%02d", minutes.toInt(), remainingSeconds.toInt())
}