package com.asu1.quizzer.viewModels.quizModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class Quiz1ViewModel : BaseQuizViewModel<Quiz1>(){
    private val _quiz1State = MutableStateFlow(Quiz1())
    val quiz1State: StateFlow<Quiz1> = _quiz1State.asStateFlow()

    init {
        resetQuiz()
    }

    override fun resetQuiz(){
        _quiz1State.value = Quiz1()
    }

    override fun loadQuiz(quiz: Quiz1){
        _quiz1State.value = quiz
    }

    override fun viewerInit(){
        _quiz1State.value.initViewState()
    }

    override fun updateQuestion(question: String){
        _quiz1State.update {
            it.copy(question = question)
        }
    }

    override fun updateAnswerAt(index: Int, answer: String){
        if(index >= _quiz1State.value.answers.size){
            return
        }
        _quiz1State.update{
            val answers = it.answers.toMutableList()
            answers[index] = answer
            it.copy(answers = answers)
        }
    }
    override fun toggleAnsAt(index: Int){
        if(index >= _quiz1State.value.ans.size){
            return
        }
        _quiz1State.update { state ->
            val ans = state.ans.toMutableList()
            val trueCount = ans.count { it }
            if (trueCount >= _quiz1State.value.maxAnswerSelection && !ans[index]) {
                return
            }
            ans[index] = !ans[index]
            state.copy(ans = ans)
        }
    }

    fun toggleUserAnsAt(index: Int){

        _quiz1State.update{ state ->
            if(index >= state.userAns.size){
                return@update state
            }
            val userAns = state.userAns.toMutableList()
            val trueCount = userAns.count { it }
            if (trueCount >= state.maxAnswerSelection && !userAns[index]) {
                return@update state
            }
            userAns[index] = !userAns[index]
            state.shuffledAnswers
            state.copy(userAns = userAns)
        }
    }

    override fun removeAnswerAt(index: Int){
        _quiz1State.update{ state ->
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
        _quiz1State.update{
            val answers = it.answers.toMutableList()
            answers.add("")
            val ans = it.ans.toMutableList()
            ans.add(false)
            it.copy(answers = answers, ans = ans)
        }
    }
    fun toggleShuffleAnswers(){
        _quiz1State.update {
            it.copy(shuffleAnswers = !it.shuffleAnswers)
        }
    }

    fun updateMaxAnswerSelection(maxAnswerSelection: String){
        _quiz1State.update{
            val maxAnswerSelectionValue = maxAnswerSelection.toIntOrNull()
            if (maxAnswerSelection.isEmpty() || maxAnswerSelectionValue == null) {
                it.copy(maxAnswerSelection = -1) // or any default value
            } else {
                it.copy(maxAnswerSelection = maxAnswerSelectionValue)
            }
        }
    }

    override fun updateBodyState(bodyType: BodyType){
        _quiz1State.update{
            it.copy(bodyType = bodyType)
        }
    }

    override fun updateBodyText(bodyText: String){
        _quiz1State.update{
            it.copy(bodyText = bodyText)
        }
    }

    override fun updateBodyImage(image: ByteArray){
        _quiz1State.update{
            it.copy(bodyImage = image)
        }
    }

    override fun updateBodyYoutube(youtubeId: String, startTime: Int){
        _quiz1State.update{
            if(youtubeId == "DELETE"){
                it.copy(youtubeId = "", youtubeStartTime = 0)
            }else if(youtubeId == ""){
                it
            }else{
                it.copy(youtubeId = youtubeId, youtubeStartTime = startTime)
            }
        }
    }
    override fun setPoint(point: Int){
        _quiz1State.update{
            it.copy(point = point)
        }
    }

}