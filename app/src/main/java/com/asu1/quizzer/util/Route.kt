package com.asu1.quizzer.util

sealed class Route(val route: String) {
    data object Init : Route("init")
    data object Home : Route("home")
    data object Ranks : Route("ranks")
    data object Statistics : Route("statistics")
    data object Setting : Route("setting")
    data object Search : Route("search")
    data object Trends : Route("trends")
    data object Login : Route("login")
    data object PrivacyPolicy : Route("privacy_policy")
    data object RegisterPolicyAgreement : Route("register_policy_agreement")
    data object RegisterNickname : Route("register_nickname")
    data object RegisterTags : Route("register_tags")
    data object CreateQuizLayout: Route("create_quiz_layout")
}