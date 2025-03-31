package com.asu1.utils

import android.content.Context

fun Context.getAppVersion(): String {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.versionName ?: "N/A"
    } catch (e: Exception) {
        Logger.debug("Failed to get app version: $e")
        "N/A"
    }
}