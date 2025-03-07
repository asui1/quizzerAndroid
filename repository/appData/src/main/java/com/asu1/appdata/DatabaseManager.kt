package com.asu1.appdata

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.asu1.utils.Logger

fun deleteDatabase(context: Context, dbName: String) {
    val dbPath = context.getDatabasePath(dbName)
    if (dbPath.exists()) {
        Logger.debug("DB_UPDATE", "Deleting old database: $dbName")
        dbPath.delete()
    }
}

fun getCurrentDatabaseVersion(context: Context, dbName: String): Int {
    return try {
        val db = SQLiteDatabase.openDatabase(
            context.getDatabasePath(dbName).absolutePath,
            null,
            SQLiteDatabase.OPEN_READONLY
        )
        val version = db.version
        db.close()
        version
    } catch (e: Exception) {
        Logger.debug("DB_UPDATE", "Database doesn't exist: $dbName")
        0 // If database doesn't exist, assume version 0
    }
}
