package com.asu1.quizzer.util.constants

import com.asu1.quizzer.R
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle

const val BASE_URL = "https://quizzer.co.kr/api/"
val ColorList = listOf("PrimaryColor", "SecondaryColor", "TertiaryColor")
val paletteSize = PaletteStyle.entries.size
val contrastSize = Contrast.entries.size
enum class GenerateWith{
    TITLE_IMAGE, COLOR
}
val questionTypes = listOf(R.drawable.questiontype1, R.drawable.questiontype2, R.drawable.questiontype3, R.drawable.questiontype4)
val fonts = listOf("Gothic A1", "Noto Sans", "Maruburi", "Spoqahans", "EF-diary", "Ongle-Yunue", "Ongle-Eyeon")
val colors = listOf("Color1", "Color2", "Color3", "Color4", "Color5", "Color6", "Color7", "Color8", "Color9", "Color10")
val borders = listOf(R.string.no_border, R.string.underline, R.string.box, R.string.box2)
val outlines = listOf(R.string.no_outline, R.string.shadow, R.string.inverse)

