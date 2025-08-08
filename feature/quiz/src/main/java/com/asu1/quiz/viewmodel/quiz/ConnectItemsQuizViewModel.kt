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
    override val _quizState: MutableStateFlow<ConnectItemsQuiz> = MutableStateFlow(ConnectItemsQuiz())
    val leftDotOffsets  = mutableStateListOf<Offset>()
    val rightDotOffsets = mutableStateListOf<Offset>()

    init {
        resetQuiz()
    }
    fun onDragEnd(offset: Offset): Int? {
        return getDragIndex(offset, rightDotOffsets)
    }

    fun onQuiz4Update(quiz4ViewModelStates: ConnectItemsQuizAction){
        when(quiz4ViewModelStates){
            ConnectItemsQuizAction.AddLeft -> {
                this._quizState.update { it.copy().apply { addAnswer() } }
                leftDotOffsets.add(Offset.Zero)
            }
            ConnectItemsQuizAction.AddRight -> {
                this._quizState.update { it.copy().apply { addConnectionAnswer() } }
                rightDotOffsets.add(Offset.Zero)
            }
            is ConnectItemsQuizAction.RemoveLeft -> {
                if(this._quizState.value.answers.size <= 2){
                    return
                }
                this._quizState.update {
                    it.copy().apply {
                        deleteAnswerAt(quiz4ViewModelStates.index)
                    }
                }
                leftDotOffsets.removeLastOrNull()
            }
            is ConnectItemsQuizAction.RemoveRight -> {
                if(this._quizState.value.connectionAnswers.size <= 2){
                    return
                }
                this._quizState.update {
                    it.copy().apply {
                        deleteConnectionAnswerAt(quiz4ViewModelStates.index)
                    }
                }
                rightDotOffsets.removeLastOrNull()
            }
            is ConnectItemsQuizAction.UpdateLeftDotOffset -> {
                if(quiz4ViewModelStates.index in leftDotOffsets.indices)
                    leftDotOffsets[quiz4ViewModelStates.index] = quiz4ViewModelStates.offset
            }
            is ConnectItemsQuizAction.UpdateRightDotOffset -> {
                if(quiz4ViewModelStates.index in rightDotOffsets.indices)
                    rightDotOffsets[quiz4ViewModelStates.index] = quiz4ViewModelStates.offset
            }
            is ConnectItemsQuizAction.OnDragEndCreator -> {
                this._quizState.update {
                    it.copy().apply {
                        connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                            this[quiz4ViewModelStates.from] = onDragEnd(quiz4ViewModelStates.offset)
                        }
                    }
                }
            }
            is ConnectItemsQuizAction.OnDragEndViewer -> {
                this._quizState.update {
                    it.copy().apply {
                        userConnectionIndex[quiz4ViewModelStates.from] = onDragEnd(quiz4ViewModelStates.offset)
                    }
                }
            }
            is ConnectItemsQuizAction.ResetConnectionCreator -> {
                this._quizState.update {
                    it.copy().apply{
                        connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                            this.set(quiz4ViewModelStates.index, null)
                        }
                    }
                }
            }
            is ConnectItemsQuizAction.ResetConnectionViewer -> {
                this._quizState.update {
                    it.copy().apply{
                        userConnectionIndex[quiz4ViewModelStates.index] = null
                    }
                }
            }
            is ConnectItemsQuizAction.UpdateLeftAnswerAt -> {
                this._quizState.update {
                    it.copy().apply{
                        answers = answers.toMutableList().apply {
                            this[quiz4ViewModelStates.index] = quiz4ViewModelStates.text
                        }
                    }
                }
            }
            is ConnectItemsQuizAction.UpdateRightAnswerAt -> {
                this._quizState.update {
                    it.copy().apply{
                        connectionAnswers = connectionAnswers.toMutableList().apply {
                            this[quiz4ViewModelStates.index] = quiz4ViewModelStates.text
                        }
                    }
                }
            }
        }
    }

    override fun viewerInit() {
        this._quizState.value.initViewState()
    }

    override fun loadQuiz(quiz: ConnectItemsQuiz) {
        this._quizState.value = quiz
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
        this._quizState.value = ConnectItemsQuiz()
        this.leftDotOffsets.apply {
            clear()
            repeat(_quizState.value.answers.size) { add(Offset.Zero) }
        }
        this.rightDotOffsets.apply {
            clear()
            repeat(_quizState.value.connectionAnswers.size) { add(Offset.Zero) }
        }
    }
}
