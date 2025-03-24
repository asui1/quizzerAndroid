package com.asu1.quizzer.musics

sealed class HomeUiEvents {
    object PlayPause : HomeUiEvents()
    object Pause : HomeUiEvents()
    object Play : HomeUiEvents()
    data class CurrentAudioChanged(val index: Int) : HomeUiEvents()
    data class SeekTo(val position: Float) : HomeUiEvents()
    data class UpdateProgress(val progress: Float) : HomeUiEvents()
    object SeekToNext : HomeUiEvents()
    object SeekToPrevious : HomeUiEvents()
    object Backward : HomeUiEvents()
    object Forward : HomeUiEvents()
    data class ChangeItemOrder(val from: Int, val to: Int) : HomeUiEvents()
    data class AddLocalProgress(val progress: Float) : HomeUiEvents()
    data class UpdateLocalProgress(val progress: Float) : HomeUiEvents()
    object PushLocalProgress : HomeUiEvents()
}