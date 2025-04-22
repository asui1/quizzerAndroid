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
    val dbFile = context.getDatabasePath(dbName)
    if (!dbFile.exists()) {
        Logger.debug("DB_UPDATE", "Database doesn't exist: $dbName")
        return 0
    }

    SQLiteDatabase.openDatabase(
        dbFile.absolutePath,
        null,
        SQLiteDatabase.OPEN_READONLY
    ).use { db ->
        return db.version
    }
}
