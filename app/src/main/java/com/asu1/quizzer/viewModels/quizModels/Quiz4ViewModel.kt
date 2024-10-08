package com.asu1.quizzer.viewModels.quizModels

import com.asu1.quizzer.model.Quiz4
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Quiz4ViewModel: BaseQuizViewModel<Quiz4>() {
    private val _quiz4State = MutableStateFlow(Quiz4())
    val quiz4State: StateFlow<Quiz4> = _quiz4State.asStateFlow()

    init {
        resetQuiz()
    }

    override fun loadQuiz(quiz: Quiz4) {
        _quiz4State.value = quiz
    }

    override fun resetQuiz() {
        _quiz4State.value = Quiz4()
    }

    override fun updateAnswerAt(index: Int, answer: String) {
        _quiz4State.value = _quiz4State.value.copy(answers = _quiz4State.value.answers.toMutableList().apply {
            if(index >= size){
                add(answer)
            }else{
                set(index, answer)
            }
        })
    }

    override fun toggleAnsAt(index: Int) {
        //NOT USED IN QUIZ4
    }

    override fun removeAnswerAt(index: Int) {
        if(_quiz4State.value.answers.size >= index){
            return
        }
        if(_quiz4State.value.answers.size <= 3){
            return
        }

        _quiz4State.value = _quiz4State.value.copy(
            answers = _quiz4State.value.answers.toMutableList().apply {
                removeAt(index)
            },
            connectionAnswers = _quiz4State.value.connectionAnswers.toMutableList().apply {
                removeAt(index)
            },
            connectionAnswerIndex = _quiz4State.value.connectionAnswerIndex.toMutableList().apply {
                if (contains(index)) {
                    set(index, null)
                }
                removeAt(index)
            },
        )
    }

    override fun addAnswer() {
        _quiz4State.value = _quiz4State.value.copy(
            answers = _quiz4State.value.answers.toMutableList().apply {
                add("")
            },
            connectionAnswers = _quiz4State.value.connectionAnswers.toMutableList().apply {
                add("")
            },
        )

    }

    override fun updateQuestion(question: String) {
        _quiz4State.value = _quiz4State.value.copy(question = question)
    }

    fun updateConnectionAnswerIndex(index: Int, answerIndex: Int?){
        if(index >= _quiz4State.value.answers.size){
            return
        }
        _quiz4State.value = _quiz4State.value.copy(connectionAnswerIndex = _quiz4State.value.connectionAnswerIndex.toMutableList().apply {
            set(index, answerIndex)
        })
    }

    fun updateConnectionAnswer(index: Int, answer: String){
        if(index >= _quiz4State.value.answers.size){
            return
        }
        _quiz4State.value = _quiz4State.value.copy(connectionAnswers = _quiz4State.value.connectionAnswers.toMutableList().apply {
            set(index, answer)
        })
    }

}
