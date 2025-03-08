package com.asu1.resources

import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle

const val BASE_URL = "https://quizzer.co.kr/"
const val BASE_URL_API = "${BASE_URL}api/"
val questionTypes = listOf(R.drawable.questiontype1, R.drawable.questiontype2, R.drawable.questiontype3, R.drawable.questiontype4)

val ColorList = listOf("PrimaryColor", "SecondaryColor", "TertiaryColor")
val paletteSize = PaletteStyle.entries.size
val contrastSize = Contrast.entries.size
enum class GenerateWith{
    TITLE_IMAGE, COLOR
}

val fonts = listOf("Gothic A1", "Noto Sans", "Gamja Flower", "Jua", "Sunflower", "Gasoek One", "Grandiflora One")
val colors = listOf("Color1", "Color2", "Color3", "Color4", "Color5", "Color6", "Color7", "Color8", "Color9", "Color10")
val borders = listOf(R.string.no_border, R.string.underline, R.string.box, R.string.box2)
val outlines = listOf(R.string.no_outline, R.string.shadow, R.string.inverse)

const val NOTIFICATION_ID = 1343
const val NOTIFICATION_CHANNEL_NAME = "quizzer music notification channel 1343"
const val NOTIFICATION_CHANNEL_ID = "quizzer music notification channel id 1343"

enum class TextStyles{
    QUESTION,
    ANSWER,
    BODY,
}

data class UserLoginInfo(val email: String, val nickname: String, val urlToImage: String?, val tags: Set<String>?, val agreement: Boolean)
