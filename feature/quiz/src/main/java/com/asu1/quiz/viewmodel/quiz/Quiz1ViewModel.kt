package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quiz.Quiz1
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class Quiz1ViewModel : BaseQuizViewModel<Quiz1>(Quiz1()){
    override val _quizState: MutableStateFlow<Quiz1> = MutableStateFlow(Quiz1())

    init {
        resetQuiz()
    }

    override fun resetQuiz(){
        _quizState.value = Quiz1()
    }

    override fun loadQuiz(quiz: Quiz1){
        _quizState.value = quiz
    }

    override fun viewerInit(){
        _quizState.value.initViewState()
    }

    override fun toggleAnsAt(index: Int){
        if(index >= _quizState.value.ans.size){
            return
        }
        _quizState.update { state ->
            val ans = state.ans.toMutableList()
            ans[index] = !ans[index]
            state.copy(ans = ans)
        }
    }

    override fun removeAnswerAt(index: Int){
        _quizState.update{ state ->
            if(index >= state.answers.size){
                return
            }
            if(state.answers.size <= 3){
                return
            }
            val answers = state.answers.toMutableList()
            answers.removeAt(index)
            val ans = state.ans.toMutableList()
            ans.removeAt(index)
            state.copy(answers = answers, ans = ans)
        }
    }
    override fun addAnswer(){
        _quizState.update{
            val answers = it.answers.toMutableList()
            answers.add("")
            val ans = it.ans.toMutableList()
            ans.add(false)
            it.copy(answers = answers, ans = ans)
        }
    }

    fun toggleShuffleAnswers(){
        _quizState.update {
            it.copy(shuffleAnswers = !it.shuffleAnswers)
        }
    }
}