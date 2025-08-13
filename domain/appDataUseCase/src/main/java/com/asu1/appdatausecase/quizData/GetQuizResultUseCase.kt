package com.asu1.appdatausecase.quizData

import com.asu1.quizdata.QuizDataRepository
import javax.inject.Inject

class GetQuizResultUseCase @Inject constructor(
    private val repo: QuizDataRepository
) { suspend operator fun invoke(id: String) = repo.getResult(id) }
