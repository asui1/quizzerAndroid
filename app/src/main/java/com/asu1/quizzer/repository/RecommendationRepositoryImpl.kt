package com.asu1.quizzer.repository

import com.asu1.quizzer.model.QuizCardList
import com.asu1.quizzer.model.RecommendationList
import com.asu1.quizzer.model.UserRankList
import com.asu1.quizzer.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class RecommendationRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : RecommendationRepository {
    override suspend fun getRecommendations(
        language: String,
        email: String
    ): Response<RecommendationList> {
        return apiService.getRecommendations(language, email)
    }

    override suspend fun searchQuiz(search: String): Response<QuizCardList> {
        return apiService.searchQuiz(search)
    }

    override suspend fun getMyQuiz(email: String): Response<QuizCardList> {
        return apiService.getMyQuiz(email)
    }

    override suspend fun getTrends(language: String): Response<QuizCardList> {
        return apiService.getTrends(language)
    }

    override suspend fun getUserRanks(): Response<UserRankList> {
        return apiService.getUserRanks()
    }
}