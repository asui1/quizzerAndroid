package com.asu1.quizzer.util.musics

import com.asu1.quizzer.musics.MusicAllInOne

sealed class PlayerUIState {
    object Loading : PlayerUIState()
    object Ready: PlayerUIState()
}