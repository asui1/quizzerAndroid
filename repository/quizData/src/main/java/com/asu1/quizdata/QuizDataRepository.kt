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
    suspend fun addQuiz(quizLayoutSerializer: QuizLayoutSerializer): Response<Void>
    suspend fun getQuizData(uuid: String): Response<QuizLayoutSerializer>
    suspend fun submitQuiz(sendQuizResult: SendQuizResult): Response<QuizResult>
    suspend fun getResult(resultId: String): Response<GetQuizResult>
    suspend fun getMyQuiz(email: String): Result<List<QuizCard>>
    suspend fun deleteMyQuiz(quizId: String, email: String): Result<Unit>
}
