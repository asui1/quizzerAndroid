package com.asu1.appdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.asu1.appdata.music.Mood
import com.asu1.appdata.music.Music
import com.asu1.appdata.music.MusicDao
import com.asu1.appdata.music.MusicMoodCrossRef
import com.asu1.appdata.suggestion.SearchSuggestion
import com.asu1.appdata.suggestion.SearchSuggestionDao

const val DATABASE_NAME = "quizzer_database"

@Database(
    entities = [Music::class, Mood::class, MusicMoodCrossRef::class, SearchSuggestion::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
    abstract fun searchSuggestionDao(): SearchSuggestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, prepopulateData: () -> Unit): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            prepopulateData()
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add missing table if it doesn't exist (to avoid errors)
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS search_suggestions (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, query TEXT NOT NULL, priority INTEGER NOT NULL DEFAULT 0)"
                )
            }
        }
    }
}