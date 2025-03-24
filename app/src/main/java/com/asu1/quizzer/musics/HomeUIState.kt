package com.asu1.quizzer.musics

sealed class HomeUIState{
    object InitialHome: HomeUIState()
    object HomeReady: HomeUIState()
}