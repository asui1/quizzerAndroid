package com.asu1.quizzer.viewModels.quizModels

import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz1
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


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
            ans[index] = !ans[index]
            state.copy(ans = ans)
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

    override fun updateBodyState(bodyType: BodyType){
        _quiz1State.update{
            it.copy(bodyType = bodyType)
        }
    }

    override fun updateBodyText(bodyText: String){
        _quiz1State.update{
            it.copy(bodyType = BodyType.TEXT(bodyText))
        }
    }

    override fun updateBodyImage(image: ByteArray){
        _quiz1State.update{
            it.copy(bodyType = BodyType.IMAGE(image))
        }
    }

    override fun updateBodyYoutube(youtubeId: String, startTime: Int){
        _quiz1State.update{
            if(youtubeId == "DELETE"){
                it.copy(bodyType = BodyType.YOUTUBE("", 0))
            }else if(youtubeId == ""){
                it
            }else{
                it.copy(bodyType = BodyType.YOUTUBE(youtubeId, startTime))
            }
        }
    }
    override fun setPoint(point: Int){
        _quiz1State.update{
            it.copy(point = point)
        }
    }

}