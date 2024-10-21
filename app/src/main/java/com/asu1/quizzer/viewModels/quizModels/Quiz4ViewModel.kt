package com.asu1.quizzer.viewModels.quizModels

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.model.Quiz4
import com.asu1.quizzer.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Quiz4ViewModel: BaseQuizViewModel<Quiz4>() {
    private val _quiz4State = MutableStateFlow(Quiz4())
    val quiz4State: StateFlow<Quiz4> = _quiz4State.asStateFlow()

    private val stateUpdateChannel = Channel<(Quiz4) -> Quiz4>(Channel.UNLIMITED)

    init {
        resetQuiz()
        processStateUpdates()
    }

    private fun processStateUpdates() {
        viewModelScope.launch {
            for (update in stateUpdateChannel) {
                _quiz4State.value = update(_quiz4State.value)
            }
        }
    }

    override fun viewerInit() {
        _quiz4State.value.initViewState()
    }

    override fun loadQuiz(quiz: Quiz4) {
        _quiz4State.value = quiz
    }

    override fun resetQuiz() {
        Logger().debug("Resetting quiz")
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
        if(_quiz4State.value.answers.size <= index){
            Logger().debug("Index out of range")
            return
        }
        if(_quiz4State.value.answers.size <= 3){
            Logger().debug("Cannot remove more answers")
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
                for (i in 0 until size) {
                    if(this[i] != null){
                        if(this[i] == index){
                            set(i, null)
                        } else if(this[i]!! > index){
                            set(i, this[i]!! - 1)
                        }
                    }
                }
                removeAt(index)
            },
            dotPairOffsets = _quiz4State.value.dotPairOffsets.toMutableList().apply {
                removeAt(index)
            }
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
            connectionAnswerIndex = _quiz4State.value.connectionAnswerIndex.toMutableList().apply {
                add(null)
            },
            dotPairOffsets = _quiz4State.value.dotPairOffsets.toMutableList().apply {
                add(Pair(null, null))
            }
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

    fun updateUserConnectionAnswerIndex(index: Int, answerIndex: Int?){
        if(index >= _quiz4State.value.answers.size){
            return
        }
        _quiz4State.value = _quiz4State.value.copy(userConnectionIndex = _quiz4State.value.userConnectionIndex.toMutableList().apply {
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

    fun updateDotOffset(index: Int, offset: Offset, isLeft: Boolean){
        if(index >= _quiz4State.value.answers.size){
            return
        }
        _quiz4State.value.updateOffset(index, offset, isLeft)
    }

    fun updateConnection(curIndex: Int, offset: Offset?){
        if(offset == null){
            updateConnectionAnswerIndex(curIndex, null)
        }
        else {
            val connectionIndex = getClosestDotIndex(offset)
            Logger().debug("Connection index : $connectionIndex")
            if (connectionIndex != -1) {
                updateConnectionAnswerIndex(curIndex, connectionIndex)
            }
        }
    }

    fun updateUserConnection(curIndex: Int, offset: Offset?){
        if(offset == null){
            updateUserConnectionAnswerIndex(curIndex, null)
        }
        else {
            val connectionIndex = getClosestDotIndex(offset)
            Logger().debug("Connection index : $connectionIndex")
            if (connectionIndex != -1) {
                updateUserConnectionAnswerIndex(curIndex, connectionIndex)
            }
        }
    }

    fun getClosestDotIndex(offset: Offset): Int {
        val referDistance = 3000f
        for (i in quiz4State.value.dotPairOffsets.indices) {
            val rightDot = quiz4State.value.dotPairOffsets[i].second
            if (rightDot != null) {
                Logger().debug("Right dot : $rightDot")
                val distance = Offset(
                    x = rightDot.x - offset.x,
                    y = rightDot.y - offset.y
                ).getDistanceSquared()
                Logger().debug("Distance : $distance")
                if (distance < referDistance) {
                    return i
                }
            }
        }
        return -1
    }
}
