package com.asu1.quizdata

import com.asu1.network.QuizApi
import com.asu1.network.RecommendationApi
import com.asu1.quizcardmodel.QuizCardList
import com.asu1.quizcardmodel.RecommendationList
import com.asu1.userdatamodels.UserRankList
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import javax.inject.Inject

class RecommendationRepositoryImpl @Inject constructor(
    private val quizApi: QuizApi,
    private val recommendationApi: RecommendationApi
) : RecommendationRepository {
    override suspend fun mostViewed(language: String) =
        recommendationApi.mostViewed(language)

    override suspend fun mostRecent(language: String) =
        recommendationApi.mostRecent(language)

    override suspend fun related(language: String) =
        recommendationApi.getRelated(language)

    override suspend fun getRecommendations(
        language: String,
        email: String
    ): Response<RecommendationList> {
        return recommendationApi.getRecommendations(language, email)
    }

    override suspend fun searchQuiz(search: String): Response<QuizCardList> {
        return quizApi.searchQuiz(search)
    }

    override suspend fun getMyQuiz(email: String): Response<QuizCardList> {
        return quizApi.getMyQuiz(email)
    }

    override suspend fun getTrends(language: String): Response<QuizCardList> {
        return quizApi.getTrends(language)
    }

    override suspend fun getUserRanks(): Response<UserRankList> {
        return quizApi.getUserRanks()
    }
}

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindings {
    @Binds
    abstract fun bindRecommendationRepository(
        impl: RecommendationRepositoryImpl
    ): RecommendationRepository
}
