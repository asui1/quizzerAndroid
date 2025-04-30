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
import com.asu1.appdata.stringFilter.AdminWord
import com.asu1.appdata.stringFilter.AdminWordDao
import com.asu1.appdata.stringFilter.InappropriateWord
import com.asu1.appdata.stringFilter.InappropriateWordDao
import com.asu1.appdata.suggestion.SearchSuggestion
import com.asu1.appdata.suggestion.SearchSuggestionDao

const val DATABASE_NAME = "quizzer_database"
const val PREPOPULATE_DB = "quizzerDB.db"

@Database(
    entities = [Music::class, Mood::class, MusicMoodCrossRef::class, SearchSuggestion::class, AdminWord::class, InappropriateWord::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
    abstract fun searchSuggestionDao(): SearchSuggestionDao
    abstract fun adminWordDao(): AdminWordDao
    abstract fun inappropriateWordDao(): InappropriateWordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val currentVersion = getCurrentDatabaseVersion(context, DATABASE_NAME)

                if (currentVersion == 1) {
                    deleteDatabase(context, DATABASE_NAME)
                }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .createFromAsset(PREPOPULATE_DB)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add missing table if it doesn't exist (to avoid errors)
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS search_suggestions (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, query TEXT NOT NULL, priority INTEGER NOT NULL DEFAULT 0, lang TEXT NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS admin_words (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "word TEXT NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS inappropriate_words (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "word TEXT NOT NULL)"
                )
            }
        }
    }
}