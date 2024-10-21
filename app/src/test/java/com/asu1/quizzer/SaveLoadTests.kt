package com.asu1.quizzer

import androidx.compose.ui.graphics.Color
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.Quiz4
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class SaveLoadTests {

    @Test
    fun imageColorSaveTest(){
        val imageColor = ImageColor(Color.Red, byteArrayOf(), Color.Blue, ImageColorState.COLOR)
        val json = Json.encodeToString(imageColor)
        val imageColor2 = Json.decodeFromString<ImageColor>(json)
        assertEquals(imageColor, imageColor2)
    }

    @Test
    fun quiz1SaveTest(){
        val quiz1 = Quiz1(
            bodyType = BodyType.NONE,
            image = byteArrayOf(),
            bodyText = "",
            shuffleAnswers = false,
            maxAnswerSelection = 1,
            answers = mutableListOf(
                "오리아나",
                "니달리",
                "르블랑",
                "카직스",
                "애니"
            ),
            ans = mutableListOf(
                false,
                true,
                false,
                false,
                false
            ),
            question = "페이커가 데뷔전 첫 킬을 딴 챔피언은?",
            youtubeId = null.toString(),
            youtubeStartTime = 0,
        )
        val json = quiz1.changeToJson()
        val quiz12 = Quiz1()
        quiz12.load(json)
        assertEquals(quiz1, quiz12)
    }

    @Test
    fun quiz2SaveTest(){
        val quiz2 = Quiz2(
            centerDate = YearMonth.of(1996, 5),
        yearRange= 20,
        answerDate = mutableSetOf(
            LocalDate.of(1996, 5, 7),
        ),
        maxAnswerSelection = 1,
        answers = mutableListOf(),
        question = "페이커의 생일은?"

        )
        val json = quiz2.changeToJson()
        val quiz22 = Quiz2()
        quiz22.load(json)
        assertEquals(quiz2, quiz22)
    }

    @Test
    fun quiz3SaveTest(){
        val quiz3 = Quiz3(
        answers= mutableListOf(
        "Team Liquid",
        "Gen. G",
        "C9",
        "BLG",
        "LNG",
        "JDG",
        "Weibo"
        ),
        question = "2023년 월즈에서 T1이 상대한 팀 순서는?"
        )
        val json = quiz3.changeToJson()
        val quiz32 = Quiz3()
        quiz32.load(json)
        assertEquals(quiz3, quiz32)
    }

    @Test
    fun quiz4SaveTest(){
        val quiz4 = Quiz4(
            connectionAnswers = mutableListOf(
            "캡틴잭",
            "우지",
            "류",
            "나그네",
            "룰러"
            ),
        connectionAnswerIndex = mutableListOf(
        2,
        0,
        3,
        4,
        1
        ),
        answers = mutableListOf(
        "제드",
        "알리스타",
        "리븐",
        "아지르",
        "갈리오"
        ),
        question =  "페이커가 플레이한 챔피언과 연관이 있는 선수를 연결하세요."
        )
        val json = quiz4.changeToJson()
        val quiz42 = Quiz4()
        quiz42.load(json)
        assertEquals(quiz4, quiz42)
    }
}