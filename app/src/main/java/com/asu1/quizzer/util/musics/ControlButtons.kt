package com.asu1.quizzer.util.musics

sealed class ControlButtons {
    object PlayPause: ControlButtons()
    object Next: ControlButtons()
    object Rewind: ControlButtons()

}