package com.asu1.appdata.music

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction

@Dao
interface MusicDao {
    @Insert
    suspend fun insertMusic(music: Music)

    @Insert
    suspend fun insertMood(mood: Mood)

    @Insert
    suspend fun insertMusicMoodCrossRef(crossRef: MusicMoodCrossRef)

    @Query("SELECT * FROM music")
    suspend fun getAllMusic(): List<Music>

    @Transaction
    @Query("SELECT * FROM music WHERE title = :title")
    suspend fun getMusicWithMoods(title: String): List<MusicWithMoods>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("""
        SELECT * FROM music 
        INNER JOIN MusicMoodCrossRef ON music.title = MusicMoodCrossRef.title 
        WHERE MusicMoodCrossRef.mood = :mood
    """)
    suspend fun getMusicByMood(mood: String): List<MusicWithMoods>

    @Query("SELECT COUNT(*) > 0 FROM mood WHERE mood = :mood")
    suspend fun isMoodExists(mood: String): Boolean

    @Query("SELECT COUNT(*) > 0 FROM music WHERE title = :title")
    suspend fun isMusicExists(title: String): Boolean

    @Query("SELECT COUNT(*) > 0 FROM MusicMoodCrossRef WHERE title = :title AND mood = :mood")
    suspend fun isMusicMoodCrossRefExists(title: String, mood: String): Boolean


}