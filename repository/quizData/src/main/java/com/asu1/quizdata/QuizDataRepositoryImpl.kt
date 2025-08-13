package com.asu1.quizdata

import com.asu1.models.quiz.GetQuizResult
import com.asu1.models.quiz.QuizResult
import com.asu1.models.quiz.SendQuizResult
import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.network.IoDispatcher
import com.asu1.network.QuizApi
import com.asu1.network.runApi
import com.asu1.quizcardmodel.QuizCard
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class QuizDataRepositoryImpl @Inject constructor(
    private val quizApi: QuizApi,
    @IoDispatcher private val io: CoroutineDispatcher
) : QuizDataRepository {
    override suspend fun deleteQuiz(uuid: String, email: String): Response<Void> {
        return quizApi.deleteQuiz(uuid, email)
    }

    override suspend fun addQuiz(quizLayoutSerializer: QuizLayoutSerializer): Response<Void> {
        return quizApi.addQuiz(quizLayoutSerializer)
    }

    override suspend fun getQuizData(uuid: String): Response<QuizLayoutSerializer> {
        return quizApi.getQuizData(uuid)
    }

    override suspend fun submitQuiz(sendQuizResult: SendQuizResult): Response<QuizResult> {
        return quizApi.submitQuiz(sendQuizResult)
    }

    override suspend fun getResult(resultId: String): Response<GetQuizResult> {
        return quizApi.getResult(resultId)
    }
    override suspend fun getMyQuiz(email: String): Result<List<QuizCard>> = withContext(io) {
        runApi { quizApi.getMyQuiz(email) }                 // Result<Response<QuizCardList>>
            .mapCatching { resp ->
                if (!resp.isSuccessful) throw HttpException(resp)
                resp.body()?.searchResult ?: emptyList()
            }
    }

    override suspend fun deleteMyQuiz(
        quizId: String,
        email: String
    ): Result<Unit> = withContext(io) {
        runApi { quizApi.deleteQuiz(quizId, email) }        // Result<Response<Void>>
            .mapCatching { resp ->
                if (!resp.isSuccessful) throw HttpException(resp)
            }
            .map { }                                         // Result<Unit>
    }
}
