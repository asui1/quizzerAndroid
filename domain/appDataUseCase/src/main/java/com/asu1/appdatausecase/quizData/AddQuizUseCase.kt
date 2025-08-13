package com.asu1.appdatausecase.quizData

import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.quizdata.QuizDataRepository
import javax.inject.Inject

class AddQuizUseCase @Inject constructor(
    private val repo: QuizDataRepository
) { suspend operator fun invoke(layout: QuizLayoutSerializer) = repo.addQuiz(layout) }
