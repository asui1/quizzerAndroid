package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.ConnectItemsQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class ConnectItemsQuizViewModel : BaseQuizViewModel<ConnectItemsQuiz>(
    ConnectItemsQuiz()
) {
    override val _quizState: MutableStateFlow<ConnectItemsQuiz> = MutableStateFlow(ConnectItemsQuiz())

    init {
        resetQuiz()
    }

    fun onQuiz4Update(quiz4ViewModelStates: ConnectItemsQuizViewModelStates){
        when(quiz4ViewModelStates){
            ConnectItemsQuizViewModelStates.AddLeft -> this._quizState.update { it.copy().apply { addAnswer() } }
            ConnectItemsQuizViewModelStates.AddRight -> this._quizState.update { it.copy().apply { addConnectionAnswer() } }
            is ConnectItemsQuizViewModelStates.RemoveLeft -> {
                if(this._quizState.value.answers.size <= 2){
                    return
                }
                this._quizState.update {
                    it.copy().apply {
                        deleteAnswerAt(quiz4ViewModelStates.index)
                    }
                }
            }
            is ConnectItemsQuizViewModelStates.RemoveRight -> {
                if(this._quizState.value.answers.size <= 2){
                    return
                }
                this._quizState.update {
                    it.copy().apply {
                        deleteConnectionAnswerAt(quiz4ViewModelStates.index)
                    }
                }
            }
            is ConnectItemsQuizViewModelStates.UpdateLeftDotOffset -> {
                this._quizState.update {
                    it.copy().apply{
                        leftDots = leftDots.toMutableList().apply {
                            this[quiz4ViewModelStates.index] = quiz4ViewModelStates.offset
                        }
                    }
                }
            }
            is ConnectItemsQuizViewModelStates.UpdateRightDotOffset -> {
                this._quizState.update {
                    it.copy().apply{
                        rightDots = rightDots.toMutableList().apply {
                            this.set(
                                quiz4ViewModelStates.index,
                                quiz4ViewModelStates.offset
                            )
                        }
                    }
                }
            }
            is ConnectItemsQuizViewModelStates.OnDragEndCreator -> {
                this._quizState.update {
                    it.copy().apply {
                        onDragEnd(quiz4ViewModelStates.from, quiz4ViewModelStates.offset, true)
                    }
                }
            }
            is ConnectItemsQuizViewModelStates.OnDragEndViewer -> {
                this._quizState.update {
                    it.copy().apply {
                        onDragEnd(quiz4ViewModelStates.from, quiz4ViewModelStates.offset, false)
                    }
                }
            }
            is ConnectItemsQuizViewModelStates.ResetConnectionCreator -> {
                this._quizState.update {
                    it.copy().apply{
                        connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                            this.set(quiz4ViewModelStates.index, null)
                        }
                    }
                }
            }
            is ConnectItemsQuizViewModelStates.ResetConnectionViewer -> {
                this._quizState.update {
                    it.copy().apply{
                        userConnectionIndex[quiz4ViewModelStates.index] = null
                    }
                }
            }
        }
    }

    fun updateAnswerAt(index: Int, answer: String){
        this._quizState.update {
            it.copy().apply {
                answers = answers.toMutableList().apply {
                    this[index] = answer
                }
            }
        }
    }

    fun updateConnectionAnswerAt(index: Int, answer: String){
        this._quizState.update {
            it.copy().apply {
                connectionAnswers = connectionAnswers.toMutableList().apply {
                    this[index] = answer
                }
            }
        }
    }

    override fun viewerInit() {
        this._quizState.value.initViewState()
    }

    override fun loadQuiz(quiz: ConnectItemsQuiz) {
        this._quizState.value = quiz
    }

    override fun resetQuiz() {
        this._quizState.value = ConnectItemsQuiz()
    }
}
