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

    var connectionAnswers: List<String> = mutableListOf("", "", "", ""),
    var connectionAnswerIndex: List<Int?> = mutableListOf(null, null, null, null),
    var dotPairOffsets: List<Pair<Offset?, Offset?>> = mutableListOf(Pair(null, null), Pair(null, null), Pair(null, null), Pair(null, null)),
    var userConnectionIndex: List<Int?> = mutableListOf(null, null, null, null),
    override val layoutType: QuizType = QuizType.QUIZ4,
    override var answers: MutableList<String> = mutableListOf("", "", "", ""),
    override var question: String = "",
    override var point: Int = 5,
    override var bodyType: BodyType = BodyType.NONE
): Quiz(answers, question, point, bodyType) {
    fun updateUserConnection(from: Int, to: Int?){
        userConnectionIndex = userConnectionIndex.toMutableList().apply {
            set(from, to)
        }
    }

    fun onDragEnd(from: Int, offset: Offset){
        if(dotPairOffsets[0].second == null) return
        if(abs(offset.x - (dotPairOffsets[0].second!!.x)) > 10) return
        for(i in dotPairOffsets.indices){
            if(dotPairOffsets[i].second == null) continue
            else if(abs(offset.y - dotPairOffsets[i].second!!.y) < 10) {
                updateUserConnection(
                    from = from,
                    to = i,
                )
            }
        }
    }

    override fun initViewState() {
        userConnectionIndex = MutableList(answers.size) { null }
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
    fun updateOffset(index: Int, offset: Offset, isStart: Boolean){
        dotPairOffsets = dotPairOffsets.toMutableList().apply {
            set(index, if(isStart){
                Pair(offset, get(index).second)
            }else{
                Pair(get(index).first, offset)
            })
        }
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