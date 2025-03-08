package com.asu1.appdata.stringFilter

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminWordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAdminWord(adminWord: AdminWord)

    @Query("SELECT word FROM admin_words")
    suspend fun getAllWords(): List<String>

    @Query("SELECT *, `rowid` FROM admin_words WHERE word MATCH :searchQuery")
    fun searchAdminWords(searchQuery: String): Flow<List<AdminWord>>
}

@Dao
interface InappropriateWordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInappropriateWord(word: InappropriateWord)

    @Query("SELECT word FROM inappropriate_words")
    suspend fun getAllWords(): List<String>

    @Query("SELECT *, `rowid` FROM inappropriate_words WHERE word MATCH :searchQuery")
    fun searchInappropriateWords(searchQuery: String): Flow<List<InappropriateWord>>
}
