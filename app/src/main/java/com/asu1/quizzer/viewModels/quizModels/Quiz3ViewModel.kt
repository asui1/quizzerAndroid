package com.asu1.quizzer.viewModels.quizModels

import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz3
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Quiz3ViewModel: BaseQuizViewModel<Quiz3>() {
    private val _quiz3State = MutableStateFlow(Quiz3())
    val quiz3State: StateFlow<Quiz3> get() = _quiz3State.asStateFlow()

    init {
        resetQuiz()
    }

    override fun viewerInit() {
        _quiz3State.value.initViewState()
    }

    override fun loadQuiz(quiz: Quiz3) {
        _quiz3State.value = quiz
    }

    override fun resetQuiz() {
        _quiz3State.value = Quiz3()
    }

    override fun updateAnswerAt(index: Int, answer: String) {
        _quiz3State.value = _quiz3State.value.copy(answers = _quiz3State.value.answers.toMutableList().apply {
            if(index >= size){
                add(answer)
            }else{
                set(index, answer)
            }
        })
    }

    override fun toggleAnsAt(index: Int) {
        //NOT USED IN QUIZ3
    }

    override fun removeAnswerAt(index: Int) {
        if(_quiz3State.value.answers.size <= 3){
            return
        }
        _quiz3State.value = _quiz3State.value.copy(answers = _quiz3State.value.answers.toMutableList().apply {
            if(index < size){
                removeAt(index)
            }
        })
    }

    override fun addAnswer() {
        _quiz3State.value = _quiz3State.value.copy(answers = _quiz3State.value.answers.toMutableList().apply {
            add("")
        })
    }

    override fun updateQuestion(question: String) {
        _quiz3State.value = _quiz3State.value.copy(question = question)
    }

    fun updateShuffledAnswers(shuffledAnswers: List<String>){
        val newShuffledAnswers = mutableListOf(_quiz3State.value.shuffledAnswers[0]) + shuffledAnswers
        _quiz3State.value = _quiz3State.value.copy(shuffledAnswers = newShuffledAnswers.toMutableList())
    }

    fun switchShuffledAnswers(from: Int, to: Int){
        _quiz3State.value = _quiz3State.value.copy(shuffledAnswers = _quiz3State.value.shuffledAnswers.toMutableList().apply {
            add(to, removeAt(from))
        })
    }

    override fun updateBodyState(bodyType: BodyType){
        _quiz3State.value = _quiz3State.value.copy(bodyType = bodyType)
    }

    override fun updateBodyText(bodyText: String){
        _quiz3State.value = _quiz3State.value.copy(bodyType = BodyType.TEXT(bodyText = bodyText))
    }

    override fun updateBodyImage(image: ByteArray){
        _quiz3State.value = _quiz3State.value.copy(bodyType = BodyType.IMAGE(bodyImage = image))
    }

    override fun updateBodyYoutube(youtubeId: String, startTime: Int){
        if(youtubeId == "DELETE"){
            _quiz3State.value = _quiz3State.value.copy(bodyType = BodyType.YOUTUBE("", 0))
            return
        }
        if(youtubeId == ""){
            return
        }
        _quiz3State.value = _quiz3State.value.copy(bodyType = BodyType.YOUTUBE(youtubeId = youtubeId, youtubeStartTime = startTime))
    }
    override fun setPoint(point: Int){
        _quiz3State.update{
            it.copy(point = point)
        }
    }

}
