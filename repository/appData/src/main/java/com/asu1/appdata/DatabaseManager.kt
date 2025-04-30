package com.asu1.appdata

import android.content.Context
import android.database.sqlite.SQLiteDatabase

fun deleteDatabase(context: Context, dbName: String) {
    val dbPath = context.getDatabasePath(dbName)
    if (dbPath.exists()) {
        dbPath.delete()
    }
}

fun getCurrentDatabaseVersion(context: Context, dbName: String): Int {
    val dbFile = context.getDatabasePath(dbName)
    if (!dbFile.exists()) {
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
