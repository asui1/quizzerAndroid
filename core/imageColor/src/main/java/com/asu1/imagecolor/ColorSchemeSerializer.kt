package com.asu1.imagecolor

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
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

    private fun JsonObject.getColorOrDefault(key: String, default: Color): Color =
        this[key]?.jsonPrimitive?.contentOrNull
            ?.let { runCatching { it.toColor() }.getOrNull() }
            ?: default
    private fun JsonObject.applyOverrides(base: ColorScheme): ColorScheme = base.copy(
        primary = getColorOrDefault("primary", base.primary),
        onPrimary = getColorOrDefault("onPrimary", base.onPrimary),
        primaryContainer = getColorOrDefault("primaryContainer", base.primaryContainer),
        onPrimaryContainer = getColorOrDefault("onPrimaryContainer", base.onPrimaryContainer),
        secondary = getColorOrDefault("secondary", base.secondary),
        onSecondary = getColorOrDefault("onSecondary", base.onSecondary),
        secondaryContainer = getColorOrDefault("secondaryContainer", base.secondaryContainer),
        onSecondaryContainer = getColorOrDefault("onSecondaryContainer", base.onSecondaryContainer),
        tertiary = getColorOrDefault("tertiary", base.tertiary),
        onTertiary = getColorOrDefault("onTertiary", base.onTertiary),
        tertiaryContainer = getColorOrDefault("tertiaryContainer", base.tertiaryContainer),
        onTertiaryContainer = getColorOrDefault("onTertiaryContainer", base.onTertiaryContainer),
        error = getColorOrDefault("error", base.error),
        onError = getColorOrDefault("onError", base.onError),
        errorContainer = getColorOrDefault("errorContainer", base.errorContainer),
        onErrorContainer = getColorOrDefault("onErrorContainer", base.onErrorContainer),
        surface = getColorOrDefault("surface", base.surface),
        onSurface = getColorOrDefault("onSurface", base.onSurface),
        surfaceVariant = getColorOrDefault("surfaceVariant", base.surfaceVariant),
        onSurfaceVariant = getColorOrDefault("onSurfaceVariant", base.onSurfaceVariant),
        outline = getColorOrDefault("outline", base.outline),
        outlineVariant = getColorOrDefault("outlineVariant", base.outlineVariant),
        scrim = getColorOrDefault("scrim", base.scrim),
        inverseSurface = getColorOrDefault("inverseSurface", base.inverseSurface),
        inversePrimary = getColorOrDefault("inversePrimary", base.inversePrimary),
        surfaceTint = getColorOrDefault("surfaceTint", base.surfaceTint)
    )

    override fun deserialize(decoder: Decoder): ColorScheme {
        val json = decoder.decodeSerializableValue(JsonObject.serializer())
        val brightness = json["brightness"]?.jsonPrimitive?.contentOrNull
        val baseColorScheme = if (brightness == "Brightness.dark") {
            com.asu1.resources.DarkColorScheme
        } else {
            com.asu1.resources.LightColorScheme
        }
        return json.applyOverrides(baseColorScheme)
    }

    @Suppress("LongParameterList")
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
}
