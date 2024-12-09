package com.asu1.quizzer.util

import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle

val ColorList = listOf("PrimaryColor", "SecondaryColor", "TertiaryColor")
val paletteSize = PaletteStyle.entries.size
val contrastSize = Contrast.entries.size
enum class GenerateWith{
    TITLE_IMAGE, COLOR
}
