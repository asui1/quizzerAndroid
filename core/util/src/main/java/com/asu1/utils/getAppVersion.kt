package com.asu1.utils

import android.content.Context
import android.content.pm.PackageManager

fun Context.getAppVersion(): String {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.versionName ?: "N/A"
    } catch (e: PackageManager.NameNotFoundException) {
        Logger.debug("App package not found: ${e.message}")
        "N/A"
    }
}
