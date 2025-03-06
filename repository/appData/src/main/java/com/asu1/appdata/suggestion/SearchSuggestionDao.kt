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

    // FAST Partial Matching using FTS MATCH instead of LIKE
    @Query("SELECT *, `rowid` FROM search_suggestions ORDER BY priority DESC LIMIT 8")
    fun getTopPrioritySuggestions(): Flow<List<SearchSuggestion>>

    @Query("SELECT *, `rowid` FROM search_suggestions WHERE `query` MATCH :searchText ORDER BY priority DESC LIMIT 8")
    fun getFilteredSuggestions(searchText: String): Flow<List<SearchSuggestion>>

    @Query("UPDATE search_suggestions SET priority = CASE WHEN priority < :maxPriority THEN priority + 1 ELSE priority END WHERE `query` = :query")
    suspend fun increasePriority(query: String, maxPriority: Int)

    @Query("UPDATE search_suggestions SET priority = CASE WHEN priority > 1 THEN priority - 1 ELSE 1 END")
    suspend fun decayOldPriorities()

    @Query("DELETE FROM search_suggestions")
    suspend fun clearAll()
}
