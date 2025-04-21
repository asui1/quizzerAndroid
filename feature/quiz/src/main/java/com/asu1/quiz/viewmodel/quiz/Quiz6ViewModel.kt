package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quiz.Quiz6
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class Quiz6ViewModel : BaseQuizViewModel<Quiz6>(Quiz6()){
    override val _quizState: MutableStateFlow<Quiz6> = MutableStateFlow(Quiz6())

    init {
        resetQuiz()
    }

    override fun resetQuiz(){
        _quizState.value = Quiz6()
    }

    override fun loadQuiz(quiz: Quiz6){
        _quizState.value = quiz
    }

    override fun viewerInit(){
        _quizState.value.initViewState()
    }

    override fun toggleAnsAt(index: Int){
// NOT USED IN QUIZ6
    }

    override fun removeAnswerAt(index: Int){
        _quizState.update{ state ->
            if (index >= state.fillInAnswers.size || index + 1 >= state.answers.size) {
                return@update state
            }

            val newFillInAnswers = state.fillInAnswers.toMutableList().also {
                it.removeAt(index)
            }

            val newAnswers = state.answers.toMutableList().also {
                val merged = it[index] + it[index + 1]
                it[index] = merged
                it.removeAt(index + 1)
            }

            state.copy(
                answers = newAnswers,
                fillInAnswers = newFillInAnswers
            )
        }
    }

    fun addAnswer(index: Int){
        _quizState.update { state ->
            val answers = state.answers.toMutableList()
            val fillIns = state.fillInAnswers.toMutableList()

            // index 유효성 체크: 0 <= index < answers.size
            if (index < 0 || index >= answers.size) return@update state

            // answers 리스트에 빈 텍스트 삽입
            answers.add(index, "")

            // fillInAnswers 리스트에도 같은 위치에 대응되는 빈칸 삽입
            fillIns.add(index, "") // index 번째 빈칸이 생김 (answers[index]와 answers[index + 1] 사이)

            state.copy(
                answers = answers,
                fillInAnswers = fillIns
            )
        }
    }

    override fun addAnswer(){
        addAnswer(_quizState.value.answers.size-1)
    }

}