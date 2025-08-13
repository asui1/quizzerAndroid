package com.asu1.appdatausecase.quizData

import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizdata.QuizDataRepository
import javax.inject.Inject

class GetMyQuizUseCase @Inject constructor(
    private val repo: QuizDataRepository
) {
    suspend operator fun invoke(email: String): Result<List<QuizCard>> =
        repo.getMyQuiz(email)
}
