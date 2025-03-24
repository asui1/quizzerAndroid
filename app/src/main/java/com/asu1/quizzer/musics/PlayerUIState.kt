package com.asu1.quizzer.musics

sealed class PlayerUIState {
    object Loading : PlayerUIState()
    object Ready: PlayerUIState()
}