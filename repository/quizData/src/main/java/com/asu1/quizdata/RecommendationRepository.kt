// RecommendationRepository.kt
package com.asu1.quizdata

import com.asu1.quizcardmodel.QuizCardList
import com.asu1.quizcardmodel.RecommendationList
import com.asu1.userdatamodels.UserRankList
import retrofit2.Response

interface RecommendationRepository {
    suspend fun getRecommendations(language: String, email: String): Response<RecommendationList>
    suspend fun searchQuiz(search: String): Response<QuizCardList>
    suspend fun getMyQuiz(email: String): Response<QuizCardList>
    suspend fun getTrends(language: String): Response<QuizCardList>
    suspend fun getUserRanks(): Response<UserRankList>
}
