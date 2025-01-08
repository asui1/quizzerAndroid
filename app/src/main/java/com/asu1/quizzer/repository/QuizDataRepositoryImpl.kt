package com.asu1.quizzer.repository

import com.asu1.quizzer.data.GetQuizResult
import com.asu1.quizzer.data.QuizLayoutSerializer
import com.asu1.quizzer.data.QuizResult
import com.asu1.quizzer.data.SendQuizResult
import com.asu1.quizzer.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class QuizDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
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