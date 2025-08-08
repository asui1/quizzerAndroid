package com.asu1.quiz.viewmodel.quiz

import androidx.compose.ui.geometry.Offset

sealed class ConnectItemsQuizAction {
    data object AddLeft : ConnectItemsQuizAction()
    data object AddRight: ConnectItemsQuizAction()
    data class RemoveLeft(val index: Int): ConnectItemsQuizAction()
    data class RemoveRight(val index: Int): ConnectItemsQuizAction()
    data class UpdateLeftDotOffset(val index: Int, val offset: Offset): ConnectItemsQuizAction()
    data class UpdateRightDotOffset(val index: Int, val offset: Offset): ConnectItemsQuizAction()
    data class OnDragEndCreator(val from: Int, val offset: Offset): ConnectItemsQuizAction()
    data class OnDragEndViewer(val from: Int, val offset: Offset): ConnectItemsQuizAction()
    data class ResetConnectionCreator(val index: Int): ConnectItemsQuizAction()
    data class ResetConnectionViewer(val index: Int): ConnectItemsQuizAction()
    data class UpdateLeftAnswerAt(val index: Int, val text: String): ConnectItemsQuizAction()
    data class UpdateRightAnswerAt(val index: Int, val text: String): ConnectItemsQuizAction()
}
