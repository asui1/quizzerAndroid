package com.asu1.quizzer.repository

import com.asu1.models.quiz.GetQuizResult
import com.asu1.models.quiz.QuizResult
import com.asu1.models.quiz.SendQuizResult
import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.network.QuizApi
import retrofit2.Response
import javax.inject.Inject

class QuizDataRepositoryImpl @Inject constructor(
    private val quizApi: QuizApi
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
}
