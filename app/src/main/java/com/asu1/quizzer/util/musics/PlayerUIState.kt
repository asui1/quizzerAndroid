package com.asu1.quizzer.util.musics

sealed class PlayerUIState {
    object Loading : PlayerUIState()
    object Ready: PlayerUIState()
}