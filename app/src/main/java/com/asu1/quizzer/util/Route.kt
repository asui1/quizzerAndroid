package com.asu1.quizzer.util

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Init : Route
    @Serializable
    data object Home : Route
    @Serializable
    data object Ranks : Route
    @Serializable
    data object Statistics : Route
    @Serializable
    data object Setting : Route
    @Serializable
    data object Search : Route
    @Serializable
    data object Trends : Route
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
}

