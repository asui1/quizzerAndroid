package com.asu1.quizzer.quizCreateUtils

import com.asu1.models.quiz.QuizData
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.sampleQuizList
import com.asu1.models.scorecard.ScoreCard
import com.asu1.quizzer.quizCreateUtils.datacreation.iuQuizData1
import com.asu1.quizzer.quizCreateUtils.datacreation.iuQuizTheme1
import com.asu1.quizzer.quizCreateUtils.datacreation.iuScoreCard
import com.asu1.quizzer.test.R

data class QuizBundle(
    val data: QuizData,
    val theme: QuizTheme,
    val quizzes: List<Quiz>,
    val scoreCard: ScoreCard,
    val titleImage: Int,
    val overlayImage: Int = 0,
)

@Suppress("unused")
val sampleQuizBundle = QuizBundle(
    data         = iuQuizData1,
    theme        = iuQuizTheme1,
    quizzes      = sampleQuizList,
    titleImage   = R.drawable.iu_ai1,
    overlayImage = R.drawable.iu_portrait_bg2,
    scoreCard = iuScoreCard,
)