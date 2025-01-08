// QuizRepository.kt
package com.asu1.quizzer.repository

import com.asu1.quizzer.data.*
import com.asu1.quizzer.model.QuizCardList
import com.asu1.quizzer.model.UserRankList
import retrofit2.Response

// Manage all items related to Quiz's data.
interface QuizDataRepository {
    suspend fun deleteQuiz(uuid: String, email: String): Response<Void>
    suspend fun addQuiz(quizLayoutSerializer: QuizLayoutSerializer): Response<Void>
    suspend fun getQuizData(uuid: String): Response<QuizLayoutSerializer>
    suspend fun submitQuiz(sendQuizResult: SendQuizResult): Response<QuizResult>
    suspend fun getResult(resultId: String): Response<GetQuizResult>
}