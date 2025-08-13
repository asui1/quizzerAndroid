package com.asu1.appdatausecase.quizData

import com.asu1.quizdata.QuizDataRepository
import javax.inject.Inject

class GetQuizDataUseCase @Inject constructor(
    private val repo: QuizDataRepository
) { suspend operator fun invoke(id: String) = repo.getQuizData(id) }
