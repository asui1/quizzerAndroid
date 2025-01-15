package com.asu1.quizzer.util.musics

sealed class MediaStateEvents {
    object PlayPause : MediaStateEvents()
    object Play : MediaStateEvents()
    object Pause : MediaStateEvents()
    object SeekToNext : MediaStateEvents()
    object SeekToPrevious : MediaStateEvents()
    object SeekTo : MediaStateEvents()
    object Backward : MediaStateEvents()
    object Forward : MediaStateEvents()
    object Stop : MediaStateEvents()
    object SelectedMusicChange : MediaStateEvents()
    data class MediaProgress(val progress: Float) : MediaStateEvents()
    data class ChangeItemOrder(val from: Int, val to: Int) : MediaStateEvents()
}