package com.asu1.quizzer.util

import com.asu1.quizzer.model.QuizType
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Init : Route
    @Serializable
    data object Home : Route
    @Serializable
    data object Setting : Route
    @Serializable
    data object Search : Route
    @Serializable
    data object Login : Route
    @Serializable
    data object PrivacyPolicy : Route
    @Serializable
    data class RegisterPolicyAgreement(val email: String, val profileUri:String?) : Route
    @Serializable
    data object RegisterNickname : Route
    @Serializable
    data object RegisterTags : Route
    @Serializable
    data object CreateQuizLayout: Route
    @Serializable
    data object QuizBuilder: Route
    @Serializable
    data class QuizCaller(val loadIndex: Int, val quizType: QuizType, val insertIndex: Int) : Route
    @Serializable
    data class QuizSolver(val initIndex: Int) : Route
    @Serializable
    data object DesignScoreCard: Route
    @Serializable
    data object LoadLocalQuiz: Route
    @Serializable
    data object LoadUserQuiz: Route
}

