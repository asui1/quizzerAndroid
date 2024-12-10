package com.asu1.quizzer.util.constants

import com.asu1.quizzer.R
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle

val ColorList = listOf("PrimaryColor", "SecondaryColor", "TertiaryColor")
val paletteSize = PaletteStyle.entries.size
val contrastSize = Contrast.entries.size
enum class GenerateWith{
    TITLE_IMAGE, COLOR
}
val questionTypes = listOf(R.drawable.questiontype1, R.drawable.questiontype2, R.drawable.questiontype3, R.drawable.questiontype4)
