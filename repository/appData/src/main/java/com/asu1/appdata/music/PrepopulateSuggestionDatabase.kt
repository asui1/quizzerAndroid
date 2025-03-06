package com.asu1.appdata.music

import com.asu1.appdata.suggestion.SearchSuggestion
import com.asu1.appdata.suggestion.SearchSuggestionRepository
import kotlinx.coroutines.flow.first

suspend fun prepopulateSuggestionDatabase(
    suggestionSuggestionRepository: SearchSuggestionRepository
){
    if (suggestionSuggestionRepository.getTopPrioritySuggestions().first().isNotEmpty()) {
        return
    }

    // 🔍 Prepopulate Search Suggestions
    val searchSuggestions = listOf(
        SearchSuggestion(query = "Kotlin Basics", priority = 5),
        SearchSuggestion(query = "Room Database", priority = 5),
        SearchSuggestion(query = "Jetpack Compose", priority = 4),
        SearchSuggestion(query = "Android Architecture", priority = 3)
    )

    for (suggestion in searchSuggestions) {
        suggestionSuggestionRepository.insertSuggestion(suggestion)
    }
}