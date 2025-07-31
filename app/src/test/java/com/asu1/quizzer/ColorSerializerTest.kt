package com.asu1.quizzer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.asu1.imagecolor.ColorSchemeSerializer.toColor
import com.asu1.imagecolor.ColorSchemeSerializer.toHexString
import org.junit.Assert.assertEquals
import org.junit.Test

class ColorSerializerTest {

    @Test
    fun toHexStringtoColor_withValidColor_matchesOriginalColor() {
        val originalColor = Color(0xFFc1ecd5)

        val hexText = originalColor.toArgb().toHexString()

        // Deserialize the color
        val deserializedColor = hexText.toColor()

        // Assert that the original and deserialized colors are the same
        assertEquals(originalColor, deserializedColor)
    }

    @Test
    fun toHexString_withValidColor_returnsCorrectHexString() {
        val color = Color(0xFFc1ecd5)
        val hexString = color.toArgb().toHexString()
        assertEquals("#ffc1ecd5", hexString)
    }

    @Test
    fun toColor_withValidHexString_returnsCorrectColor() {
        val hexString = "#ffc1ecd5"
        val color = hexString.toColor()
        assertEquals(Color(0xFFc1ecd5), color)
    }

}
