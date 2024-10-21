package com.asu1.quizzer.data

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
        val json = JsonObject(
            mapOf(
                "brightness" to JsonPrimitive("Brightness.light"), // Assuming brightness is always light
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

    override fun deserialize(decoder: Decoder): ColorScheme {
        val json = decoder.decodeSerializableValue(JsonObject.serializer())
        return ColorScheme(
            primary = Color(json["primary"]!!.jsonPrimitive.content.toColorInt()),
            onPrimary = Color(json["onPrimary"]!!.jsonPrimitive.content.toColorInt()),
            primaryContainer = Color(json["primaryContainer"]!!.jsonPrimitive.content.toColorInt()),
            onPrimaryContainer = Color(json["onPrimaryContainer"]!!.jsonPrimitive.content.toColorInt()),
            secondary = Color(json["secondary"]!!.jsonPrimitive.content.toColorInt()),
            onSecondary = Color(json["onSecondary"]!!.jsonPrimitive.content.toColorInt()),
            secondaryContainer = Color(json["secondaryContainer"]!!.jsonPrimitive.content.toColorInt()),
            onSecondaryContainer = Color(json["onSecondaryContainer"]!!.jsonPrimitive.content.toColorInt()),
            tertiary = Color(json["tertiary"]!!.jsonPrimitive.content.toColorInt()),
            onTertiary = Color(json["onTertiary"]!!.jsonPrimitive.content.toColorInt()),
            tertiaryContainer = Color(json["tertiaryContainer"]!!.jsonPrimitive.content.toColorInt()),
            onTertiaryContainer = Color(json["onTertiaryContainer"]!!.jsonPrimitive.content.toColorInt()),
            error = Color(json["error"]!!.jsonPrimitive.content.toColorInt()),
            onError = Color(json["onError"]!!.jsonPrimitive.content.toColorInt()),
            errorContainer = Color(json["errorContainer"]!!.jsonPrimitive.content.toColorInt()),
            onErrorContainer = Color(json["onErrorContainer"]!!.jsonPrimitive.content.toColorInt()),
            surface = Color(json["surface"]!!.jsonPrimitive.content.toColorInt()),
            onSurface = Color(json["onSurface"]!!.jsonPrimitive.content.toColorInt()),
            surfaceVariant = Color(json["surfaceVariant"]!!.jsonPrimitive.content.toColorInt()),
            onSurfaceVariant = Color(json["onSurfaceVariant"]!!.jsonPrimitive.content.toColorInt()),
            outline = Color(json["outline"]!!.jsonPrimitive.content.toColorInt()),
            outlineVariant = Color(json["outlineVariant"]!!.jsonPrimitive.content.toColorInt()),
            scrim = Color(json["scrim"]!!.jsonPrimitive.content.toColorInt()),
            inverseSurface = Color(json["inverseSurface"]!!.jsonPrimitive.content.toColorInt()),
            inversePrimary = Color(json["inversePrimary"]!!.jsonPrimitive.content.toColorInt()),
            surfaceTint = Color(json["surfaceTint"]!!.jsonPrimitive.content.toColorInt()),
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

    private fun Int.toHexString() = "#${Integer.toHexString(this)}"
    private fun String.toColorInt() = Color(android.graphics.Color.parseColor(this)).toArgb()
}