package com.asu1.quizzer.util

import com.asu1.quizzer.model.QuizType
import com.asu1.quizzer.util.Route.QuizSolver
import com.asu1.quizzer.util.Route.ScoringScreen
import com.asu1.quizzer.util.Route.Search
import kotlinx.serialization.Serializable

fun fromRouteName(route: String?, query: String): Route?{
    return when(route){
        "Search" -> Search(query)
        "QuizSolver" -> QuizSolver(0, query)
        "ScoringScreen" -> ScoringScreen
        else -> null
    }
}

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
    data class QuizSolver(val initIndex: Int, val uuid: String = "") : Route
    @Serializable
    data object DesignScoreCard: Route
    @Serializable
    data object LoadLocalQuiz: Route
    @Serializable
    data object LoadUserQuiz: Route
    @Serializable
    data object ScoringScreen: Route
}

