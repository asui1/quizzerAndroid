package com.asu1.appdatausecase

import com.asu1.network.IoDispatcher
import com.asu1.quizcardmodel.QuizCardsWithTag
import com.asu1.quizdata.RecommendationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class FetchHomeRecommendationsUseCase @Inject constructor(
    private val repo: RecommendationRepository,
    @param:IoDispatcher private val io: CoroutineDispatcher
) {
    suspend operator fun invoke(language: String): Result<List<QuizCardsWithTag>> =
        withContext(io) {
            runCatching {
                coroutineScope {
                    val mv = async { repo.mostViewed(language) }
                    val mr = async { repo.mostRecent(language) }
                    val rl = async { repo.related(language) }

                    listOf(mv.await(), mr.await(), rl.await())
                        .mapNotNull { resp ->
                            if (!resp.isSuccessful) throw HttpException(resp)
                            resp.body()
                        }
                        .map { QuizCardsWithTag(it.key, it.items) }
                        .filter { it.quizCards.isNotEmpty() }
                }
            }
        }
}
