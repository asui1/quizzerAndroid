package com.asu1.quizzer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.asu1.quizzer.data.ColorSchemeSerializer.toColor
import com.asu1.quizzer.data.ColorSchemeSerializer.toHexString
import com.asu1.quizzer.data.ColorSerializer
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.generateUniqueId
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class ColorSerializerTest {

    @Test
    fun testGenerateUniqueId() {
        val originalColor = Color(0xFFc1ecd5)
        val json = Json { encodeDefaults = true }

        // Serialize the color
        val serializedColor = json.encodeToString(ColorSerializer, originalColor)

        val hexText = originalColor.toArgb().toHexString()

        java.util.logging.Logger.getLogger("Serialized").info("GEN UNIQUE ID $serializedColor")
        java.util.logging.Logger.getLogger("Serialized").info("GEN UNIQUE ID $hexText")

        // Deserialize the color
        val deserializedColor = hexText.toColor()

        // Assert that the original and deserialized colors are the same
        assertEquals(originalColor, deserializedColor)
    }

}