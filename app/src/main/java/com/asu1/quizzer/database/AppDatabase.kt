package com.asu1.quizzer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asu1.quizzer.musics.Mood
import com.asu1.quizzer.musics.Music
import com.asu1.quizzer.musics.MusicDao
import com.asu1.quizzer.musics.MusicMoodCrossRef

@Database(entities = [Music::class, Mood::class, MusicMoodCrossRef::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
}