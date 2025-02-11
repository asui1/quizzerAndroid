package com.asu1.quizzer.viewModels.quizModels

import com.asu1.models.quiz.Quiz4
import com.asu1.models.serializers.BodyType
import com.asu1.quizzer.model.Quiz4ViewModelStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Quiz4ViewModel: BaseQuizViewModel<Quiz4>() {
    private val _quiz4State = MutableStateFlow(Quiz4())
    val quiz4State: StateFlow<Quiz4> get() = _quiz4State.asStateFlow()

    init {
        resetQuiz()
    }

    fun onQuiz4Update(quiz4ViewModelStates: Quiz4ViewModelStates){
        when(quiz4ViewModelStates){
            Quiz4ViewModelStates.AddLeft -> _quiz4State.update { it.copy().apply { addAnswer() } }
            Quiz4ViewModelStates.AddRight -> _quiz4State.update { it.copy().apply { addConnectionAnswer() } }
            is Quiz4ViewModelStates.RemoveLeft -> {
                if(_quiz4State.value.answers.size <= 2){
                    return
                }
                _quiz4State.update {
                    it.copy().apply {
                        deleteAnswerAt(quiz4ViewModelStates.index)
                    }
                }
            }
            is Quiz4ViewModelStates.RemoveRight -> {
                if(_quiz4State.value.answers.size <= 2){
                    return
                }
                _quiz4State.update {
                    it.copy().apply {
                        deleteConnectionAnswerAt(quiz4ViewModelStates.index)
                    }
                }
            }
            is Quiz4ViewModelStates.UpdateLeftDotOffset -> {
                _quiz4State.update {
                    it.copy().apply{
                        leftDots = leftDots.toMutableList().apply {
                            set(quiz4ViewModelStates.index, quiz4ViewModelStates.offset)
                        }
                    }
                }
            }
            is Quiz4ViewModelStates.UpdateRightDotOffset -> {
                _quiz4State.update {
                    it.copy().apply{
                        rightDots = rightDots.toMutableList().apply {
                            set(quiz4ViewModelStates.index, quiz4ViewModelStates.offset)
                        }
                    }
                }
            }
            is Quiz4ViewModelStates.OnDragEndCreator -> {
                _quiz4State.update {
                    it.copy().apply {
                        onDragEnd(quiz4ViewModelStates.from, quiz4ViewModelStates.offset, true)
                    }
                }
            }
            is Quiz4ViewModelStates.OnDragEndViewer -> {
                _quiz4State.update {
                    it.copy().apply {
                        onDragEnd(quiz4ViewModelStates.from, quiz4ViewModelStates.offset, false)
                    }
                }
            }
            is Quiz4ViewModelStates.ResetConnectionCreator -> {
                _quiz4State.update {
                    it.copy().apply{
                        connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                            set(quiz4ViewModelStates.index, null)
                        }
                    }
                }
            }
            is Quiz4ViewModelStates.ResetConnectionViewer -> {
                _quiz4State.update {
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
        _quiz4State.update {
            it.copy().apply {
                connectionAnswers = connectionAnswers.toMutableList().apply {
                    set(index, answer)
                }
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
        _quiz4State.value = Quiz4()
    }

    override fun updateAnswerAt(index: Int, answer: String) {
        _quiz4State.update {
            it.copy().apply {
                answers = answers.toMutableList().apply {
                    set(index, answer)
                }
            }
        }
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

    override fun updateQuestion(question: String) {
        _quiz4State.update {
            it.copy().apply {
                this.question = question
            }
        }
    }

    override fun updateBodyState(bodyType: BodyType){
        _quiz4State.value = _quiz4State.value.copy(bodyType = bodyType)
    }

    override fun updateBodyText(bodyText: String){
        _quiz4State.value = _quiz4State.value.copy(bodyType = BodyType.TEXT(bodyText = bodyText))
    }

    override fun updateBodyImage(image: ByteArray){
        _quiz4State.value = _quiz4State.value.copy(bodyType = BodyType.IMAGE(bodyImage = image))
    }

    override fun updateBodyYoutube(youtubeId: String, startTime: Int){
        if(youtubeId == "DELETE"){
            _quiz4State.value = _quiz4State.value.copy(bodyType = BodyType.YOUTUBE("", 0))
            return
        }
        if(youtubeId == ""){
            return
        }
        _quiz4State.value = _quiz4State.value.copy(bodyType = BodyType.YOUTUBE(youtubeId, startTime))
    }
    override fun setPoint(point: Int){
        _quiz4State.update {
            it.copy().apply {
                this.point = point
            }
        }
    }
}
