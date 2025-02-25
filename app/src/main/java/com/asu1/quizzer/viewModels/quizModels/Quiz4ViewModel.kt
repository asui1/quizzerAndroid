package com.asu1.quizzer.viewModels.quizModels

import com.asu1.models.quiz.Quiz4
import com.asu1.quizzer.model.Quiz4ViewModelStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class Quiz4ViewModel: BaseQuizViewModel<Quiz4>(Quiz4()) {
    override val _quizState: MutableStateFlow<Quiz4> = MutableStateFlow(Quiz4())

    init {
        resetQuiz()
    }

    fun onQuiz4Update(quiz4ViewModelStates: Quiz4ViewModelStates){
        when(quiz4ViewModelStates){
            Quiz4ViewModelStates.AddLeft -> this._quizState.update { it.copy().apply { addAnswer() } }
            Quiz4ViewModelStates.AddRight -> this._quizState.update { it.copy().apply { addConnectionAnswer() } }
            is Quiz4ViewModelStates.RemoveLeft -> {
                if(this._quizState.value.answers.size <= 2){
                    return
                }
                this._quizState.update {
                    it.copy().apply {
                        deleteAnswerAt(quiz4ViewModelStates.index)
                    }
                }
            }
            is Quiz4ViewModelStates.RemoveRight -> {
                if(this._quizState.value.answers.size <= 2){
                    return
                }
                this._quizState.update {
                    it.copy().apply {
                        deleteConnectionAnswerAt(quiz4ViewModelStates.index)
                    }
                }
            }
            is Quiz4ViewModelStates.UpdateLeftDotOffset -> {
                this._quizState.update {
                    it.copy().apply{
                        leftDots = leftDots.toMutableList().apply {
                            set(quiz4ViewModelStates.index, quiz4ViewModelStates.offset)
                        }
                    }
                }
            }
            is Quiz4ViewModelStates.UpdateRightDotOffset -> {
                this._quizState.update {
                    it.copy().apply{
                        rightDots = rightDots.toMutableList().apply {
                            set(quiz4ViewModelStates.index, quiz4ViewModelStates.offset)
                        }
                    }
                }
            }
            is Quiz4ViewModelStates.OnDragEndCreator -> {
                this._quizState.update {
                    it.copy().apply {
                        onDragEnd(quiz4ViewModelStates.from, quiz4ViewModelStates.offset, true)
                    }
                }
            }
            is Quiz4ViewModelStates.OnDragEndViewer -> {
                this._quizState.update {
                    it.copy().apply {
                        onDragEnd(quiz4ViewModelStates.from, quiz4ViewModelStates.offset, false)
                    }
                }
            }
            is Quiz4ViewModelStates.ResetConnectionCreator -> {
                this._quizState.update {
                    it.copy().apply{
                        connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                            set(quiz4ViewModelStates.index, null)
                        }
                    }
                }
            }
            is Quiz4ViewModelStates.ResetConnectionViewer -> {
                this._quizState.update {
                    it.copy().apply{
                        userConnectionIndex = userConnectionIndex.toMutableList().apply {
                            set(quiz4ViewModelStates.index, null)
                        }
                    }
                }
            }
        }
    }

    fun updateConnectionAnswerAt(index: Int, answer: String){
        this._quizState.update {
            it.copy().apply {
                connectionAnswers = connectionAnswers.toMutableList().apply {
                    set(index, answer)
                }
            }
        }
    }

    override fun viewerInit() {
        this._quizState.value.initViewState()
    }

    override fun loadQuiz(quiz: Quiz4) {
        this._quizState.value = quiz
    }

    override fun resetQuiz() {
        this._quizState.value = Quiz4()
    }

    override fun toggleAnsAt(index: Int) {
        //NOT USED IN QUIZ4
    }

    override fun removeAnswerAt(index: Int) {
        //NOT USED IN QUIZ4
    }

    override fun addAnswer() {
        //NOT USED IN QUIZ4
    }
}
