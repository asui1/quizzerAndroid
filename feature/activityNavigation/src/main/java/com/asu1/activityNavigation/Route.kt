package com.asu1.activityNavigation

import com.asu1.models.serializers.QuizType
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Init : Route
    @Serializable
    data object Home : Route
    @Serializable
    data class Search(val searchText: String = "") : Route
    @Serializable
    data object Login : Route
    @Serializable
    data object PrivacyPolicy : Route
    @Serializable
    data class Register(val email: String, val profileUri:String?) : Route
    @Serializable
    data object CreateQuizLayout: Route
    @Serializable
    data object QuizBuilder: Route
    @Serializable
    data class QuizCaller(val loadIndex: Int, val quizType: QuizType, val insertIndex: Int) : Route
    @Serializable
    data class QuizSolver(val uuid: String = "") : Route
    @Serializable
    data object DesignScoreCard: Route
    @Serializable
    data object LoadLocalQuiz: Route
    @Serializable
    data object LoadUserQuiz: Route
    @Serializable
    data object ScoringScreen: Route
    @Serializable
    data object MyActivities: Route
    @Serializable
    data object Notifications: Route
    @Serializable
    data object QuizChecker: Route
}