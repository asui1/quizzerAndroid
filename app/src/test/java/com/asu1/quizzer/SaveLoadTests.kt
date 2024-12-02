package com.asu1.quizzer

import androidx.compose.ui.graphics.Color
import com.asu1.quizzer.data.json
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.Quiz4
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.model.sampleImageColor
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test

class SaveLoadTests {
    @Test
    fun imageColorSaveTest(){
        val imageColor = sampleImageColor
        val json = Json.encodeToString(imageColor)
        val imageColor2 = Json.decodeFromString<ImageColor>(json)
        assertEquals(imageColor, imageColor2)
    }

    @Test
    fun quiz1SaveTest(){
        val json = json.encodeToString(quiz1.changeToJson())
        val quiz12 = Quiz1()
        quiz12.load(json)
        println(quiz1)
        println(quiz12)
        val result = quiz1.equals(quiz12)
        assertEquals(result, true)
    }

    @Test
    fun quiz2SaveTest(){
        val json = json.encodeToString(quiz2.changeToJson())
        val quiz22 = Quiz2()
        quiz22.load(json)
        assertEquals(quiz2, quiz22)
    }

    @Test
    fun quiz3SaveTest(){
        val json = json.encodeToString(quiz3.changeToJson())
        val quiz32 = Quiz3()
        quiz32.load(json)
        assertEquals(quiz3, quiz32)
    }

    @Test
    fun quiz4SaveTest(){

        val json = json.encodeToString(quiz4.changeToJson())
        val quiz42 = Quiz4()
        quiz42.load(json)
        assertEquals(quiz4, quiz42)
    }

    @Test
    fun scoreCardSaveLoadTest(){
        val json = Json.encodeToString(scoreCard)
        val scoreCard2 = Json.decodeFromString<ScoreCard>(json)
        assertEquals(scoreCard, scoreCard2)
    }

}