package com.asu1.appdata.suggestion

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchSuggestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestion: SearchSuggestion)

    @Query("SELECT *, `rowid` FROM search_suggestions WHERE lang = :lang ORDER BY priority DESC LIMIT 8")
    fun getTopPrioritySuggestions(lang: String): Flow<List<SearchSuggestion>>

    @Query("SELECT *, `rowid` FROM search_suggestions WHERE `query` LIKE '%' || :searchText || '%' AND lang = :lang ORDER BY priority DESC LIMIT 8")
    fun getFilteredSuggestions(searchText: String, lang: String): Flow<List<SearchSuggestion>>
}
