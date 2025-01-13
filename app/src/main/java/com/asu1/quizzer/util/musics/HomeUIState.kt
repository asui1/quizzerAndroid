package com.asu1.quizzer.util.musics

sealed class HomeUIState{
    object InitialHome: HomeUIState()
    object HomeReady: HomeUIState()
}