package com.asu1.quizzer.musicTests

import com.asu1.quizzer.util.musics.floatToTime
import com.asu1.quizzer.util.musics.msToTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class TimeToStringTest {

    @Test
    fun msToTime_withZeroMilliseconds_returnsZeroMinutesAndSeconds() {
        val result = msToTime(0)
        assertEquals("0:00", result)
    }

    @Test
    fun msToTime_withLessThanOneMinute_returnsCorrectSeconds() {
        val result = msToTime(45000)
        assertEquals("0:45", result)
    }

    @Test
    fun msToTime_withOneMinute_returnsOneMinuteAndZeroSeconds() {
        val result = msToTime(60000)
        assertEquals("1:00", result)
    }

    @Test
    fun msToTime_withMultipleMinutes_returnsCorrectMinutesAndSeconds() {
        val result = msToTime(125000)
        assertEquals("2:05", result)
    }

    @Test
    fun msToTime_withExactMinutes_returnsCorrectMinutesAndZeroSeconds() {
        val result = msToTime(180000)
        assertEquals("3:00", result)
    }
    @Test
    fun msToTime_withNegativeMilliseconds_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException::class.java) {
            msToTime(-1)
        }
    }

    @Test
    fun floatToTime_withZeroMilliseconds_returnsZeroMinutesAndSeconds() {
        val result = floatToTime(0f)
        assertEquals("0:00", result)
    }

    @Test
    fun floatToTime_withLessThanOneMinute_returnsCorrectSeconds() {
        val result = floatToTime(45f)
        assertEquals("0:45", result)
    }

    @Test
    fun floatToTime_withOneMinute_returnsOneMinuteAndZeroSeconds() {
        val result = floatToTime(60f)
        assertEquals("1:00", result)
    }

    @Test
    fun floatToTime_withMultipleMinutes_returnsCorrectMinutesAndSeconds() {
        val result = floatToTime(125f)
        assertEquals("2:05", result)
    }

    @Test
    fun floatToTime_withExactMinutes_returnsCorrectMinutesAndZeroSeconds() {
        val result = floatToTime(180f)
        assertEquals("3:00", result)
    }

    @Test
    fun floatToTime_withNegativeMilliseconds_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException::class.java) {
            floatToTime(-1f)
        }
    }

}
