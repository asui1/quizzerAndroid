package com.asu1.models.quiz

import androidx.compose.ui.geometry.Offset
import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizJson
import com.asu1.models.serializers.QuizType
import com.asu1.models.serializers.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.abs

data class Quiz4(
    var connectionAnswers: List<String> = listOf("", "", "", ""),
    var connectionAnswerIndex: List<Int?> = listOf(null, null, null, null),
    var leftDots: List<Offset?> = listOf(null, null, null, null),
    var rightDots: List<Offset?> = listOf(null, null, null, null),
    var userConnectionIndex: List<Int?> = listOf(null, null, null, null),
    override val layoutType: QuizType = QuizType.QUIZ4,
    override var answers: List<String> = listOf("", "", "", ""),
    override var question: String = "",
    override var point: Int = 5,
    override var bodyType: BodyType = BodyType.NONE
): Quiz(answers, question, point, bodyType) {
    fun addAnswer(){
        answers = answers.toMutableList().apply {
            add("")
            connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                add(null)
            }
            leftDots = leftDots.toMutableList().apply {
                add(null)
            }
        }
    }

    fun addConnectionAnswer(){
        connectionAnswers = connectionAnswers.toMutableList().apply {
            add("")
            rightDots = rightDots.toMutableList().apply {
                add(null)
            }
        }
    }

    fun deleteAnswerAt(index: Int){
        answers = answers.toMutableList().apply {
            removeAt(index)
            connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                removeAt(index)
            }
            leftDots = leftDots.toMutableList().apply {
                removeAt(index)
            }
        }
    }

    fun deleteConnectionAnswerAt(index: Int){
        connectionAnswers = connectionAnswers.toMutableList().apply {
            removeAt(index)
            connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                for(i in indices){
                    this[i]?.let{
                        if (it == index) {
                            this[i] = null
                        } else if (it > index) {
                            this[i] = it - 1
                        }
                    }
                }
            }
            rightDots = rightDots.toMutableList().apply {
                removeAt(index)
            }
        }
    }

    fun updateUserConnection(from: Int, to: Int?){
        userConnectionIndex = userConnectionIndex.toMutableList().apply {
            this[from] = to
        }
    }

    fun onDragEnd(from: Int, offset: Offset, isCreator: Boolean){
        if(rightDots[0] == null) return
        if(abs(offset.x - (rightDots[0]!!.x)) > 25) return
        for(i in rightDots.indices){
            if(rightDots[i] == null) continue
            if(abs(offset.y - rightDots[i]!!.y) < 25) {
                if(isCreator) {
                    connectionAnswerIndex = connectionAnswerIndex.toMutableList().apply {
                        this[from] = i
                    }
                }else{
                    userConnectionIndex = userConnectionIndex.toMutableList().apply {
                        this[from] = i
                    }
                }
                break
            }
        }
    }

    override fun initViewState() {
        userConnectionIndex = List(answers.size) { null }
        validateBody()
    }

    override fun validateQuiz(): QuizError {
        if(question == ""){
            return QuizError.EMPTY_QUESTION
        }
        if(answers.contains("") || connectionAnswers.contains("")){
            return QuizError.EMPTY_ANSWER
        }
        if(connectionAnswerIndex.none { it != null }){
            return QuizError.EMPTY_OPTION
        }
        return QuizError.NO_ERROR
    }

    override fun changeToJson(): QuizJson {
        val quiz4Json = QuizJson.Quiz4Json(
            body = QuizJson.Quiz4Body(
                connectionAnswers = connectionAnswers,
                connectionAnswerIndex = connectionAnswerIndex,
                layoutType = layoutType.value,
                maxAnswerSelection = 1,
                answers = answers,
                ans = listOf(),
                points = point,
                question = question,
                bodyValue = Json.encodeToString(bodyType),
            )
        )
        return quiz4Json
    }

    override fun load(data: String) {
        val quiz4Json = json.decodeFromString<QuizJson.Quiz4Json>(data)
        val body = quiz4Json.body

        connectionAnswers = body.connectionAnswers.toMutableList()
        connectionAnswerIndex = body.connectionAnswerIndex.toMutableList()
        answers = body.answers.toMutableList()
        question = body.question
        bodyType = Json.decodeFromString(body.bodyValue)
        point = body.points

        initViewState()
    }

    override fun gradeQuiz(): Boolean {
        for(i in connectionAnswerIndex.indices){
            if(connectionAnswerIndex[i] != userConnectionIndex[i]){
                return false
            }
        }
        return true
    }
}