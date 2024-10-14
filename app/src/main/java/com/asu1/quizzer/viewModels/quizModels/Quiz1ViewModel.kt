package com.asu1.quizzer.viewModels.quizModels

import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz1
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class Quiz1ViewModel : BaseQuizViewModel<Quiz1>(){
    private val _quiz1State = MutableStateFlow(Quiz1())
    val quiz1State: StateFlow<Quiz1> = _quiz1State.asStateFlow()

    init {
        resetQuiz()
    }

    override fun resetQuiz(){
        _quiz1State.value = Quiz1()
    }

    override fun loadQuiz(quiz1: Quiz1){
        _quiz1State.value = quiz1
    }

    override fun viewerInit(){
        _quiz1State.value.initViewState()
    }

    fun updateBodyState(bodyType: BodyType){
        _quiz1State.value = _quiz1State.value.copy(bodyType = bodyType)
    }
    override fun updateQuestion(question: String){
        _quiz1State.value = _quiz1State.value.copy(question = question)
    }
    fun updateBodyText(bodyText: String){
        _quiz1State.value = _quiz1State.value.copy(bodyText = bodyText)
    }
    fun updateBodyImage(image: ByteArray){
        _quiz1State.value = _quiz1State.value.copy(image = image)
    }
    fun updateBodyYoutube(youtubeId: String, startTime: Int){
        if(youtubeId == "DELETE"){
            _quiz1State.value = _quiz1State.value.copy(youtubeId = "", youtubeStartTime = 0)
            return
        }
        if(youtubeId == ""){
            return
        }
        _quiz1State.value = _quiz1State.value.copy(youtubeId = youtubeId, youtubeStartTime = startTime)
    }
    override fun updateAnswerAt(index: Int, answer: String){
        if(index >= _quiz1State.value.answers.size){
            return
        }
        val answers = _quiz1State.value.answers.toMutableList()
        answers[index] = answer
        _quiz1State.value = _quiz1State.value.copy(answers = answers)
    }
    override fun toggleAnsAt(index: Int){
        if(index >= _quiz1State.value.ans.size){
            return
        }
        val ans = _quiz1State.value.ans.toMutableList()
        val trueCount = ans.count { it }
        if (trueCount >= _quiz1State.value.maxAnswerSelection && !ans[index]) {
            return
        }
        ans[index] = !ans[index]
        _quiz1State.value = _quiz1State.value.copy(ans = ans)
    }

    fun toggleUserAnsAt(index: Int){
        if(index >= _quiz1State.value.userAns.size){
            return
        }
        val userAns = _quiz1State.value.userAns.toMutableList()
        val trueCount = userAns.count { it }
        if (trueCount >= _quiz1State.value.maxAnswerSelection && !userAns[index]) {
            return
        }
        userAns[index] = !userAns[index]
        _quiz1State.value = _quiz1State.value.copy(userAns = userAns)
    }

    override fun removeAnswerAt(index: Int){
        if(index >= _quiz1State.value.answers.size){
            return
        }
        if(_quiz1State.value.answers.size <= 3){
            return
        }
        val answers = _quiz1State.value.answers.toMutableList()
        answers.removeAt(index)
        val ans = _quiz1State.value.ans.toMutableList()
        ans.removeAt(index)
        _quiz1State.value = _quiz1State.value.copy(answers = answers, ans = ans)
    }
    override fun addAnswer(){
        val answers = _quiz1State.value.answers.toMutableList()
        answers.add("")
        val ans = _quiz1State.value.ans.toMutableList()
        ans.add(false)
        _quiz1State.value = _quiz1State.value.copy(answers = answers, ans = ans)
    }
    fun toggleShuffleAnswers(){
        _quiz1State.value = _quiz1State.value.copy(shuffleAnswers = !_quiz1State.value.shuffleAnswers)
    }
    fun updateMaxAnswerSelection(maxAnswerSelection: String){
        val maxAnswerSelectionValue = maxAnswerSelection.toIntOrNull()
        if (maxAnswerSelection.isEmpty() || maxAnswerSelectionValue == null) {
            _quiz1State.value = _quiz1State.value.copy(maxAnswerSelection = -1) // or any default value
        } else {
            _quiz1State.value = _quiz1State.value.copy(maxAnswerSelection = maxAnswerSelectionValue)
        }
    }
}