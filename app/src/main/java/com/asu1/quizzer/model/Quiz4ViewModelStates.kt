package com.asu1.quizzer.model

import androidx.compose.ui.geometry.Offset

sealed class Quiz4ViewModelStates {
    data object AddLeft : Quiz4ViewModelStates()
    data object AddRight: Quiz4ViewModelStates()
    data class RemoveLeft(val index: Int): Quiz4ViewModelStates()
    data class RemoveRight(val index: Int): Quiz4ViewModelStates()
    data class UpdateLeftDotOffset(val index: Int, val offset: Offset): Quiz4ViewModelStates()
    data class UpdateRightDotOffset(val index: Int, val offset: Offset): Quiz4ViewModelStates()
    data class OnDragEndCreator(val from: Int, val offset: Offset): Quiz4ViewModelStates()
    data class OnDragEndViewer(val from: Int, val offset: Offset): Quiz4ViewModelStates()
    data class ResetConnectionCreator(val index: Int): Quiz4ViewModelStates()
    data class ResetConnectionViewer(val index: Int): Quiz4ViewModelStates()
}