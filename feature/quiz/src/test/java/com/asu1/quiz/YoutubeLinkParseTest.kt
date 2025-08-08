package com.asu1.quiz

import com.asu1.quiz.content.quizBodyBuilder.parseYoutubeLink
import org.junit.Assert.assertEquals
import org.junit.Test

class YoutubeLinkParseTest {

    @Test
    fun testParseYoutubeLink_withValidLink_GetIDWithTime() {
        val link = "https://www.youtube.com/watch?v=jfGCOAwlPTE&t=8072"
        val expectedId = "jfGCOAwlPTE"
        val expectedTime = 8072

        val (id, time) = parseYoutubeLink(link)

        assertEquals(expectedId, id)
        assertEquals(expectedTime, time)
    }

    //    https://youtu.be/qvizI1krngU?t=179&si=hoPiOP9OnkpvlIe7
    @Test
    fun testParseYoutubeLink_withValidLink2_GetIdWithTime() {
        val link = "https://youtu.be/qvizI1krngU?t=179&si=hoPiOP9OnkpvlIe7"
        val expectedId = "qvizI1krngU"
        val expectedTime = 179

        val (id, time) = parseYoutubeLink(link)

        assertEquals(expectedId, id)
        assertEquals(expectedTime, time)
    }

    @Test
    fun testParseYoutubeLink_withShortenedLink_GetIdWithoutTime() {
        val link = "https://youtu.be/ZkzPRdBR5Yk?si=9gqTG-EETKpFFiiC"
        val expectedId = "ZkzPRdBR5Yk"
        val expectedTime = 0

        val (id, time) = parseYoutubeLink(link)

        assertEquals(expectedId, id)
        assertEquals(expectedTime, time)
    }

    // https://www.youtube.com/live/AkvX-E9lnBs?si=NM78tAR8WUL3jB7O&t=8236
    @Test
    fun testParseYoutubeLink_withNoTime_GetIdWithoutTime() {
        val link = "https://www.youtube.com/watch?v=jfGCOAwlPTE"
        val expectedId = "jfGCOAwlPTE"
        val expectedTime = 0

        val (id, time) = parseYoutubeLink(link)

        assertEquals(expectedId, id)
        assertEquals(expectedTime, time)
    }

    @Test
    fun testParseYoutubeLink_withLiveLink_GetIdWithTime() {
        val link = "https://www.youtube.com/live/AkvX-E9lnBs?si=NM78tAR8WUL3jB7O&t=8236"
        val expectedId = "AkvX-E9lnBs"
        val expectedTime = 8236

        val (id, time) = parseYoutubeLink(link)

        assertEquals(expectedId, id)
        assertEquals(expectedTime, time)
    }
}