package com.asu1.quizzer.data

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.asu1.quizzer.ui.theme.DarkColorScheme
import com.asu1.quizzer.ui.theme.LightColorScheme
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

object ColorSchemeSerializer : KSerializer<ColorScheme> {
    override val descriptor: SerialDescriptor = JsonObject.serializer().descriptor

    override fun serialize(encoder: Encoder, value: ColorScheme) {
        val brightness = if (isColorLight(value.background)) "Brightness.light" else "Brightness.dark"
        val json = JsonObject(
            mapOf(
                "brightness" to JsonPrimitive(brightness),
                "primary" to JsonPrimitive(value.primary.toArgb().toHexString()),
                "onPrimary" to JsonPrimitive(value.onPrimary.toArgb().toHexString()),
                "primaryContainer" to JsonPrimitive(value.primaryContainer.toArgb().toHexString()),
                "onPrimaryContainer" to JsonPrimitive(value.onPrimaryContainer.toArgb().toHexString()),
                "primaryFixed" to JsonPrimitive(value.primary.toArgb().toHexString()),
                "primaryFixedDim" to JsonPrimitive(value.primary.toArgb().toHexString()),
                "onPrimaryFixed" to JsonPrimitive(value.onPrimary.toArgb().toHexString()),
                "onPrimaryFixedVariant" to JsonPrimitive(value.onPrimary.toArgb().toHexString()),
                "secondary" to JsonPrimitive(value.secondary.toArgb().toHexString()),
                "onSecondary" to JsonPrimitive(value.onSecondary.toArgb().toHexString()),
                "secondaryContainer" to JsonPrimitive(value.secondaryContainer.toArgb().toHexString()),
                "onSecondaryContainer" to JsonPrimitive(value.onSecondaryContainer.toArgb().toHexString()),
                "secondaryFixed" to JsonPrimitive(value.secondary.toArgb().toHexString()),
                "secondaryFixedDim" to JsonPrimitive(value.secondary.toArgb().toHexString()),
                "onSecondaryFixed" to JsonPrimitive(value.onSecondary.toArgb().toHexString()),
                "onSecondaryFixedVariant" to JsonPrimitive(value.onSecondary.toArgb().toHexString()),
                "tertiary" to JsonPrimitive(value.tertiary.toArgb().toHexString()),
                "onTertiary" to JsonPrimitive(value.onTertiary.toArgb().toHexString()),
                "tertiaryContainer" to JsonPrimitive(value.tertiaryContainer.toArgb().toHexString()),
                "onTertiaryContainer" to JsonPrimitive(value.onTertiaryContainer.toArgb().toHexString()),
                "tertiaryFixed" to JsonPrimitive(value.tertiary.toArgb().toHexString()),
                "tertiaryFixedDim" to JsonPrimitive(value.tertiary.toArgb().toHexString()),
                "onTertiaryFixed" to JsonPrimitive(value.onTertiary.toArgb().toHexString()),
                "onTertiaryFixedVariant" to JsonPrimitive(value.onTertiary.toArgb().toHexString()),
                "error" to JsonPrimitive(value.error.toArgb().toHexString()),
                "onError" to JsonPrimitive(value.onError.toArgb().toHexString()),
                "errorContainer" to JsonPrimitive(value.errorContainer.toArgb().toHexString()),
                "onErrorContainer" to JsonPrimitive(value.onErrorContainer.toArgb().toHexString()),
                "surfaceDim" to JsonPrimitive(value.surface.toArgb().toHexString()),
                "surface" to JsonPrimitive(value.surface.toArgb().toHexString()),
                "onSurface" to JsonPrimitive(value.onSurface.toArgb().toHexString()),
                "surfaceBright" to JsonPrimitive(value.surface.toArgb().toHexString()),
                "surfaceContainerLowest" to JsonPrimitive(value.surface.toArgb().toHexString()),
                "surfaceContainerLow" to JsonPrimitive(value.surface.toArgb().toHexString()),
                "surfaceContainer" to JsonPrimitive(value.surface.toArgb().toHexString()),
                "surfaceContainerHigh" to JsonPrimitive(value.surface.toArgb().toHexString()),
                "surfaceContainerHighest" to JsonPrimitive(value.surface.toArgb().toHexString()),
                "onSurfaceVariant" to JsonPrimitive(value.onSurfaceVariant.toArgb().toHexString()),
                "outline" to JsonPrimitive(value.outline.toArgb().toHexString()),
                "outlineVariant" to JsonPrimitive(value.outlineVariant.toArgb().toHexString()),
                "scrim" to JsonPrimitive(value.scrim.toArgb().toHexString()),
                "inverseSurface" to JsonPrimitive(value.inverseSurface.toArgb().toHexString()),
                "inversePrimary" to JsonPrimitive(value.inversePrimary.toArgb().toHexString()),
                "surfaceTint" to JsonPrimitive(value.surfaceTint.toArgb().toHexString())
            )
        )
        encoder.encodeSerializableValue(JsonObject.serializer(), json)
    }

    fun String.toColor(): Color {
        val value = this.substring(1, 9).toLong(16)
        return Color(value.toInt())
    }

    fun stringToColor(colorString: String): Color {
        val value = colorString.substring(1, 9).toLong(16)
        val color = Color(value.toInt())
        return Color(color.red, color.green, color.blue, color.alpha)
    }
    override fun deserialize(decoder: Decoder): ColorScheme {
        val json = decoder.decodeSerializableValue(JsonObject.serializer())
        val brightness = json["brightness"]?.jsonPrimitive?.content
        val baseColorScheme = if(brightness == "Brightness.dark"){
            DarkColorScheme
        }else{
            LightColorScheme
        }
        return baseColorScheme.copy(
            primary = json["primary"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.primary,
            onPrimary = json["onPrimary"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.onPrimary,
            primaryContainer = json["primaryContainer"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.primaryContainer,
            onPrimaryContainer = json["onPrimaryContainer"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.onPrimaryContainer,
            secondary = json["secondary"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.secondary,
            onSecondary = json["onSecondary"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.onSecondary,
            secondaryContainer = json["secondaryContainer"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.secondaryContainer,
            onSecondaryContainer = json["onSecondaryContainer"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.onSecondaryContainer,
            tertiary = json["tertiary"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.tertiary,
            onTertiary = json["onTertiary"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.onTertiary,
            tertiaryContainer = json["tertiaryContainer"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.tertiaryContainer,
            onTertiaryContainer = json["onTertiaryContainer"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.onTertiaryContainer,
            error = json["error"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.error,
            onError = json["onError"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.onError,
            errorContainer = json["errorContainer"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.errorContainer,
            onErrorContainer = json["onErrorContainer"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.onErrorContainer,
            surface = json["surface"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.surface,
            onSurface = json["onSurface"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.onSurface,
            surfaceVariant = json["surfaceVariant"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.surfaceVariant,
            onSurfaceVariant = json["onSurfaceVariant"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.onSurfaceVariant,
            outline = json["outline"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.outline,
            outlineVariant = json["outlineVariant"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.outlineVariant,
            scrim = json["scrim"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.scrim,
            inverseSurface = json["inverseSurface"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.inverseSurface,
            inversePrimary = json["inversePrimary"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.inversePrimary,
            surfaceTint = json["surfaceTint"]?.jsonPrimitive?.content?.toColor() ?: baseColorScheme.surfaceTint
        )
    }

    private fun ColorScheme(
        primary: Color,
        onPrimary: Color,
        primaryContainer: Color,
        onPrimaryContainer: Color,
        secondary: Color,
        onSecondary: Color,
        secondaryContainer: Color,
        onSecondaryContainer: Color,
        tertiary: Color,
        onTertiary: Color,
        tertiaryContainer: Color,
        onTertiaryContainer: Color,
        error: Color,
        onError: Color,
        errorContainer: Color,
        onErrorContainer: Color,
        surface: Color,
        onSurface: Color,
        surfaceVariant: Color,
        onSurfaceVariant: Color,
        outline: Color,
        outlineVariant: Color,
        scrim: Color,
        inverseSurface: Color,
        inversePrimary: Color,
        surfaceTint: Color
    ): ColorScheme {
        return ColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            error = error,
            onError = onError,
            errorContainer = errorContainer,
            onErrorContainer = onErrorContainer,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            outline = outline,
            outlineVariant = outlineVariant,
            scrim = scrim,
            inverseSurface = inverseSurface,
            inversePrimary = inversePrimary,
            surfaceTint = surfaceTint
        )
    }

    private fun isColorLight(color: Color): Boolean {
        val darkness = 1 - (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
        return darkness < 0.5
    }

    fun Int.toHexString() = "#${Integer.toHexString(this)}"
    private fun String.toColorInt() = Color(android.graphics.Color.parseColor(this)).toArgb()
}