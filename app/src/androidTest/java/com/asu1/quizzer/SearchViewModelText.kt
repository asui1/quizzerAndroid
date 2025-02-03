package com.asu1.quizzer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.asu1.quizzer.viewModels.SearchViewModel
import com.asu1.utils.Logger
import org.junit.Before
import org.junit.Test

class SearchViewModelText {
    private val searchViewModel = SearchViewModel()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        searchViewModel.initRecommendations(context)
    }

    @Test
    fun testSearchText() {
        searchViewModel.setSearchText("페이커")
        searchViewModel.searchRecommendations.value?.get(0)?.let { Logger.debug(it.text) }
//        assert(searchViewModel.searchRecommendations.value?.size == 1)
    }
}