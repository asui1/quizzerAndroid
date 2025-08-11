package com.asu1.quiz.viewmodel.quiz

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.utils.getDragIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ConnectItemsQuizViewModel : BaseQuizViewModel<ConnectItemsQuiz>(
    ConnectItemsQuiz()
) {
    override val mutableQuizState: MutableStateFlow<ConnectItemsQuiz> = MutableStateFlow(ConnectItemsQuiz())
    val leftDotOffsets  = mutableStateListOf<Offset>()
    val rightDotOffsets = mutableStateListOf<Offset>()

    init {
        resetQuiz()
    }
    fun onDragEnd(offset: Offset): Int? {
        return getDragIndex(offset, rightDotOffsets)
    }

    // --- tiny utils ---
    private inline fun updateQuiz(mutator: ConnectItemsQuiz.() -> Unit) {
        mutableQuizState.update { it.copy().apply(mutator) }
    }
    private fun <T> MutableList<T>.setIfInBounds(i: Int, v: T) { if (i in indices) this[i] = v }
    private fun <T> MutableList<T>.removeIfInBounds(i: Int) { if (i in indices) removeAt(i) }
    private fun <T> MutableList<T?>.setNullIfInBounds(i: Int) { if (i in indices) this[i] = null }

    // --- action entry (flat dispatcher) ---
    fun onQuiz4Update(action: ConnectItemsQuizAction) {
        when (action) {
            ConnectItemsQuizAction.AddLeft,
            ConnectItemsQuizAction.AddRight ->
                handleAdd(action)

            is ConnectItemsQuizAction.RemoveLeft,
            is ConnectItemsQuizAction.RemoveRight ->
                handleRemove(action)

            is ConnectItemsQuizAction.UpdateLeftDotOffset,
            is ConnectItemsQuizAction.UpdateRightDotOffset ->
                handleDotOffset(action)

            is ConnectItemsQuizAction.OnDragEndCreator,
            is ConnectItemsQuizAction.OnDragEndViewer ->
                handleDragEnd(action)

            is ConnectItemsQuizAction.ResetConnectionCreator,
            is ConnectItemsQuizAction.ResetConnectionViewer ->
                handleReset(action)

            is ConnectItemsQuizAction.UpdateLeftAnswerAt,
            is ConnectItemsQuizAction.UpdateRightAnswerAt ->
                handleTextUpdate(action)
        }
    }

    /* ---------- handlers ---------- */
    private fun handleAdd(action: ConnectItemsQuizAction) = when (action) {
        ConnectItemsQuizAction.AddLeft  -> addLeft()
        ConnectItemsQuizAction.AddRight -> addRight()
        else -> Unit
    }

    private fun handleRemove(action: ConnectItemsQuizAction) = when (action) {
        is ConnectItemsQuizAction.RemoveLeft  -> removeLeft(action.index)
        is ConnectItemsQuizAction.RemoveRight -> removeRight(action.index)
        else -> Unit
    }

    private fun handleDotOffset(action: ConnectItemsQuizAction) = when (action) {
        is ConnectItemsQuizAction.UpdateLeftDotOffset  ->
            leftDotOffsets.setIfInBounds(action.index, action.offset)
        is ConnectItemsQuizAction.UpdateRightDotOffset ->
            rightDotOffsets.setIfInBounds(action.index, action.offset)
        else -> Unit
    }

    private fun handleDragEnd(action: ConnectItemsQuizAction) = when (action) {
        is ConnectItemsQuizAction.OnDragEndCreator -> updateQuiz {
            connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                setIfInBounds(action.from, onDragEnd(action.offset))
            }
        }
        is ConnectItemsQuizAction.OnDragEndViewer -> updateQuiz {
            userConnectionIndex.setIfInBounds(action.from, onDragEnd(action.offset))
        }
        else -> Unit
    }

    private fun handleReset(action: ConnectItemsQuizAction) = when (action) {
        is ConnectItemsQuizAction.ResetConnectionCreator -> updateQuiz {
            connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                setNullIfInBounds(action.index)
            }
        }
        is ConnectItemsQuizAction.ResetConnectionViewer -> updateQuiz {
            userConnectionIndex.setNullIfInBounds(action.index)
        }
        else -> Unit
    }

    private fun handleTextUpdate(action: ConnectItemsQuizAction) = when (action) {
        is ConnectItemsQuizAction.UpdateLeftAnswerAt -> updateQuiz {
            answers = answers.toMutableList().apply {
                setIfInBounds(action.index, action.text)
            }
        }
        is ConnectItemsQuizAction.UpdateRightAnswerAt -> updateQuiz {
            connectionAnswers = connectionAnswers.toMutableList().apply {
                setIfInBounds(action.index, action.text)
            }
        }
        else -> Unit
    }

    // --- helpers below keep main function short ---
    private fun addLeft() {
        updateQuiz { addAnswer() }
        leftDotOffsets.add(Offset.Zero)
    }

    private fun addRight() {
        updateQuiz { addConnectionAnswer() }
        rightDotOffsets.add(Offset.Zero)
    }

    private fun removeLeft(index: Int) {
        if (mutableQuizState.value.answers.size <= 2) return
        updateQuiz { deleteAnswerAt(index) }
        leftDotOffsets.removeIfInBounds(index) // ✅ keep dots aligned
    }

    private fun removeRight(index: Int) {
        if (mutableQuizState.value.connectionAnswers.size <= 2) return
        updateQuiz { deleteConnectionAnswerAt(index) }
        rightDotOffsets.removeIfInBounds(index) // ✅ keep dots aligned
    }

    override fun viewerInit() {
        this.mutableQuizState.value.initViewState()
    }

    override fun loadQuiz(quiz: ConnectItemsQuiz) {
        this.mutableQuizState.value = quiz
        this.leftDotOffsets.apply {
            clear()
            repeat(quiz.answers.size) { add(Offset.Zero) }
        }
        this.rightDotOffsets.apply {
            clear()
            repeat(quiz.connectionAnswers.size) { add(Offset.Zero) }
        }
    }

    override fun resetQuiz() {
        this.mutableQuizState.value = ConnectItemsQuiz()
        this.leftDotOffsets.apply {
            clear()
            repeat(mutableQuizState.value.answers.size) { add(Offset.Zero) }
        }
        this.rightDotOffsets.apply {
            clear()
            repeat(mutableQuizState.value.connectionAnswers.size) { add(Offset.Zero) }
        }
    }
}
