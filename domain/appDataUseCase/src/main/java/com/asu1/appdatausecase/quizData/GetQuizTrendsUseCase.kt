package com.asu1.appdatausecase.quizData

import com.asu1.network.IoDispatcher
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizdata.RecommendationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

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
