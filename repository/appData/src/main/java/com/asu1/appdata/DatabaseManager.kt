package com.asu1.appdata

import android.content.Context
import com.asu1.utils.Logger

fun copyDatabaseFromAsset(context: Context, dbName: String, assetFileName: String) {
    val dbPath = context.getDatabasePath(dbName)

    if (dbPath.exists()) {
        Logger.debug("DB_UPDATE", "Deleting old database: $dbName")
        dbPath.delete()
    }

    context.assets.open(assetFileName).use { inputStream ->
        dbPath.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }

    Logger.debug("DB_UPDATE", "Database copied from assets: $assetFileName")
}
