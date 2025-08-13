// QuizRepository.kt
package com.asu1.quizdata

import com.asu1.models.quiz.GetQuizResult
import com.asu1.models.quiz.QuizResult
import com.asu1.models.quiz.SendQuizResult
import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.quizcardmodel.QuizCard
import retrofit2.Response

// Manage all items related to Quiz's data.
interface QuizDataRepository {
    suspend fun deleteQuiz(uuid: String, email: String): Response<Void>
    suspend fun addQuiz(layout: QuizLayoutSerializer): Result<Unit>
    suspend fun getQuizData(quizId: String): Result<QuizLayoutSerializer>
    suspend fun submitQuiz(payload: SendQuizResult): Result<QuizResult>
    suspend fun getResult(resultId: String): Result<GetQuizResult>
    suspend fun getMyQuiz(email: String): Result<List<QuizCard>>
    suspend fun deleteMyQuiz(quizId: String, email: String): Result<Unit>
}
