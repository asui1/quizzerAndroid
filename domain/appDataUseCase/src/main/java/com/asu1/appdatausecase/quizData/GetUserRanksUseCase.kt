package com.asu1.appdatausecase.quizData

import com.asu1.network.IoDispatcher
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizdata.RecommendationRepository
import com.asu1.userdatamodels.UserRank
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class GetUserRanksUseCase @Inject constructor(
    private val repo: RecommendationRepository,
    @IoDispatcher private val io: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<List<UserRank>> = withContext(io) {
        runCatching {
            val resp = repo.getUserRanks()
            if (!resp.isSuccessful) throw HttpException(resp)
            resp.body()?.searchResult ?: throw NoSuchElementException("Empty body")
        }
    }
}

class GetQuizTrendsUseCase @Inject constructor(
    private val repo: RecommendationRepository,
    @IoDispatcher private val io: CoroutineDispatcher
) {
    suspend operator fun invoke(language: String): Result<List<QuizCard>> = withContext(io) {
        runCatching {
            val resp = repo.getTrends(language)
            if (!resp.isSuccessful) throw HttpException(resp)
            resp.body()?.searchResult ?: emptyList()
        }
    }
}

class SearchQuizCardsUseCase @Inject constructor(
    private val repo: RecommendationRepository,
    @IoDispatcher private val io: CoroutineDispatcher
) {
    suspend operator fun invoke(query: String): Result<List<QuizCard>> = withContext(io) {
        runCatching {
            val resp = repo.searchQuiz(query)
            if (!resp.isSuccessful) throw HttpException(resp)
            // map body to your domain model as needed
            resp.body()?.searchResult ?: emptyList()
        }
    }
}
