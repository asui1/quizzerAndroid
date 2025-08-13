package com.asu1.appdatausecase.quizData

import com.asu1.models.quiz.SendQuizResult
import com.asu1.quizdata.QuizDataRepository
import jakarta.inject.Inject

class SubmitQuizUseCase @Inject constructor(
    private val repo: QuizDataRepository
) { suspend operator fun invoke(payload: SendQuizResult) = repo.submitQuiz(payload) }
