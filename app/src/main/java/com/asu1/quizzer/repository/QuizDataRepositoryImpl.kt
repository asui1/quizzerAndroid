package com.asu1.quizzer.repository

import com.asu1.models.quiz.GetQuizResult
import com.asu1.models.quiz.QuizResult
import com.asu1.models.quiz.SendQuizResult
import com.asu1.models.serializers.QuizLayoutSerializer
import retrofit2.Response
import javax.inject.Inject

class QuizDataRepositoryImpl @Inject constructor(
    private val apiService: com.asu1.network.ApiService
) : QuizDataRepository {
    override suspend fun deleteQuiz(uuid: String, email: String): Response<Void> {
        return apiService.deleteQuiz(uuid, email)
    }

    override suspend fun addQuiz(quizLayoutSerializer: QuizLayoutSerializer): Response<Void> {
        return apiService.addQuiz(quizLayoutSerializer)
    }

    override suspend fun getQuizData(uuid: String): Response<QuizLayoutSerializer> {
        return apiService.getQuizData(uuid)
    }

    override suspend fun submitQuiz(sendQuizResult: SendQuizResult): Response<QuizResult> {
        return apiService.submitQuiz(sendQuizResult)
    }

    override suspend fun getResult(resultId: String): Response<GetQuizResult> {
        return apiService.getResult(resultId)
    }
}