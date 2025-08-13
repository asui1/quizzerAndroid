package com.asu1.appdatausecase.quizData

import com.asu1.quizdata.QuizDataRepository
import javax.inject.Inject

class DeleteMyQuizUseCase @Inject constructor(
    private val repo: QuizDataRepository
) {
    suspend operator fun invoke(quizId: String, email: String): Result<Unit> =
        repo.deleteMyQuiz(quizId, email)
}
