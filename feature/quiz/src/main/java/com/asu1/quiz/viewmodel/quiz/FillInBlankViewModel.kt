package com.asu1.quiz.viewmodel.quiz

import com.asu1.models.quizRefactor.FillInBlankQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FillInBlankViewModel: BaseQuizViewModel<FillInBlankQuiz>(
    FillInBlankQuiz()
) {
    override val _quizState: MutableStateFlow<FillInBlankQuiz> = MutableStateFlow(FillInBlankQuiz())

    init {
        resetQuiz()
    }
    override fun loadQuiz(quiz: FillInBlankQuiz) {
        this._quizState.value = quiz
    }

    override fun resetQuiz() {
        this._quizState.value = FillInBlankQuiz()
    }

    override fun viewerInit() {
        _quizState.value.initViewState()
    }
    fun removeAnswerAt(index: Int){
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

    fun addAnswer(){
        addAnswer(_quizState.value.answers.size-1)
    }
}
