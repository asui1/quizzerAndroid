package com.asu1.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

@Composable
fun rememberAppVersion(): String {
    val context = LocalContext.current
    val view = LocalView.current

    if (view.isInEditMode) {
        return "Preview"
    }

    return context.safeAppVersion()
}

fun Context.safeAppVersion(): String {
    return try {
        val pkgInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }
        pkgInfo.versionName ?: "N/A"
    } catch (e: PackageManager.NameNotFoundException) {
        Logger.debug("App package not found: ${e.message}")
        "N/A"
    }
}
