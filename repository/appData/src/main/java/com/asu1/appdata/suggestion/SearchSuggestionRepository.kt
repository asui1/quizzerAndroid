package com.asu1.appdata.suggestion

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchSuggestionRepository @Inject constructor(
    private val dao: SearchSuggestionDao
) {

    suspend fun insertSuggestion(suggestion: SearchSuggestion) = withContext(Dispatchers.IO) {
        dao.insertSuggestion(suggestion)
    }

    fun getTopPrioritySuggestions(): Flow<List<SearchSuggestion>> {
        return dao.getTopPrioritySuggestions()
    }

    fun getFilteredSuggestions(searchText: String): Flow<List<SearchSuggestion>> {
        return if (searchText.isEmpty()) {
            getTopPrioritySuggestions()
        } else {
            dao.getFilteredSuggestions(searchText)
        }
    }


    suspend fun increasePriority(query: String, maxPriority: Int) = withContext(Dispatchers.IO) {
        dao.increasePriority(query, maxPriority)
    }

    suspend fun decayOldPriorities() = withContext(Dispatchers.IO) {
        dao.decayOldPriorities()
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        dao.clearAll()
    }
}

