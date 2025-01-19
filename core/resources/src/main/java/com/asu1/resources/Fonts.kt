package com.asu1.resources

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

val EfDiary = FontFamily(
    Font(R.font.ef_diary),
)
val GothicA1 = FontFamily(
    Font(resId = R.font.gothica1_bold, weight = FontWeight.Bold),
    Font(resId = R.font.gothica1_regular, weight = FontWeight.Normal),
    Font(resId = R.font.gothica1_light, weight = FontWeight.Light),
)

val Maruburi = FontFamily(
    Font(resId = R.font.maruburi, weight = FontWeight.Normal),
    Font(resId = R.font.maruburi_bold, weight = FontWeight.Bold),
    Font(resId = R.font.maruburi_light, weight = FontWeight.Light),
)
val NotoSans = FontFamily(
    Font(resId = R.font.notosanskr_regular, weight = FontWeight.Normal),
    Font(resId = R.font.notosanskr_light, weight = FontWeight.Light),
    Font(resId = R.font.notosanskr_bold, weight = FontWeight.Bold),
)

val Ongle_Eyeon = FontFamily(
    Font(resId = R.font.ongle_eyeon),
)

val Ongle_Yunue = FontFamily(
    Font(resId = R.font.ongle_yunue),
)

val SpoqaHans = FontFamily(
    Font(resId = R.font.spoqahansansneo, weight = FontWeight.Normal),
    Font(resId = R.font.spoqahansansneo_bold, weight = FontWeight.Bold),
    Font(resId = R.font.spoqahansansneo_light, weight = FontWeight.Light),
    )

fun getFontFamily(selection: Int): FontFamily{
    return when(selection){
        0 -> GothicA1
        1 -> NotoSans
        2 -> Maruburi
        3 -> SpoqaHans
        4 -> EfDiary
        5 -> Ongle_Yunue
        6 -> Ongle_Eyeon
        else -> GothicA1
    }
}