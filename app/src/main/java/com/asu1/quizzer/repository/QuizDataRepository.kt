// QuizRepository.kt
package com.asu1.quizzer.repository

import com.asu1.quizzer.data.GetQuizResult
import com.asu1.quizzer.data.QuizLayoutSerializer
import com.asu1.quizzer.data.QuizResult
import com.asu1.quizzer.data.SendQuizResult
import retrofit2.Response

// Manage all items related to Quiz's data.
interface QuizDataRepository {
    suspend fun deleteQuiz(uuid: String, email: String): Response<Void>
    suspend fun addQuiz(quizLayoutSerializer: QuizLayoutSerializer): Response<Void>
    suspend fun getQuizData(uuid: String): Response<QuizLayoutSerializer>
    suspend fun submitQuiz(sendQuizResult: SendQuizResult): Response<QuizResult>
    suspend fun getResult(resultId: String): Response<GetQuizResult>
}