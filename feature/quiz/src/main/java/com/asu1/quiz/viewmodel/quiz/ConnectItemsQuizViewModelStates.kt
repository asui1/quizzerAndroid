package com.asu1.quiz.viewmodel.quiz

import androidx.compose.ui.geometry.Offset

sealed class ConnectItemsQuizViewModelStates {
    data object AddLeft : ConnectItemsQuizViewModelStates()
    data object AddRight: ConnectItemsQuizViewModelStates()
    data class RemoveLeft(val index: Int): ConnectItemsQuizViewModelStates()
    data class RemoveRight(val index: Int): ConnectItemsQuizViewModelStates()
    data class UpdateLeftDotOffset(val index: Int, val offset: Offset): ConnectItemsQuizViewModelStates()
    data class UpdateRightDotOffset(val index: Int, val offset: Offset): ConnectItemsQuizViewModelStates()
    data class OnDragEndCreator(val from: Int, val offset: Offset): ConnectItemsQuizViewModelStates()
    data class OnDragEndViewer(val from: Int, val offset: Offset): ConnectItemsQuizViewModelStates()
    data class ResetConnectionCreator(val index: Int): ConnectItemsQuizViewModelStates()
    data class ResetConnectionViewer(val index: Int): ConnectItemsQuizViewModelStates()
}