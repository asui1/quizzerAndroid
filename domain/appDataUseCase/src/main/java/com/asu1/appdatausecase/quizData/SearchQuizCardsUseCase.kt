package com.asu1.appdatausecase.quizData

import com.asu1.network.IoDispatcher
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizdata.RecommendationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

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
