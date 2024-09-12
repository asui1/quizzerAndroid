package com.asu1.quizzer.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.asu1.quizzer.R

val efdiary = FontFamily(
    Font(R.font.ef_diary),
)
val gothica1 = FontFamily(
    Font(resId = R.font.gothica1_black, weight = FontWeight.Black),
    Font(resId = R.font.gothica1_bold, weight = FontWeight.Bold),
    Font(resId = R.font.gothica1_medium, weight = FontWeight.Medium),
    Font(resId = R.font.gothica1_regular, weight = FontWeight.Normal),
    Font(resId = R.font.gothica1_light, weight = FontWeight.Light),
    Font(resId = R.font.gothica1_extrabold, weight = FontWeight.ExtraBold),
    Font(resId = R.font.gothica1_extralight, weight = FontWeight.ExtraLight),
    Font(resId = R.font.gothica1_thin, weight = FontWeight.Thin),
    Font(resId = R.font.gothica1_semibold, weight = FontWeight.SemiBold),
)
val maruburi = FontFamily(
    Font(resId = R.font.maruburi, weight = FontWeight.Normal),
    Font(resId = R.font.maruburi_bold, weight = FontWeight.Bold),
    Font(resId = R.font.maruburi_extralight, weight = FontWeight.ExtraLight),
    Font(resId = R.font.maruburi_light, weight = FontWeight.Light),
    Font(resId = R.font.maruburi_semibold, weight = FontWeight.SemiBold),
)
val notosans = FontFamily(
    Font(resId = R.font.notosanskr_bold, weight = FontWeight.Bold),
    Font(resId = R.font.notosanskr_regular, weight = FontWeight.Normal),
    Font(resId = R.font.notosanskr_light, weight = FontWeight.Light),
    Font(resId = R.font.notosanskr_medium, weight = FontWeight.Medium),
    Font(resId = R.font.notosanskr_semibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.notosanskr_black, weight = FontWeight.Black),
    Font(resId = R.font.notosanskr_extrabold, weight = FontWeight.ExtraBold),
    Font(resId = R.font.notosanskr_extralight, weight = FontWeight.ExtraLight),
    Font(resId = R.font.notosanskr_thin, weight = FontWeight.Thin),
    )

val ongle_eyeon = FontFamily(
    Font(resId = R.font.ongle_eyeon),
)

val ongle_yunue = FontFamily(
    Font(resId = R.font.ongle_yunue),
)

val spoqahans = FontFamily(
    Font(resId = R.font.spoqahansansneo, weight = FontWeight.Normal),
    Font(resId = R.font.spoqahansansneo_bold, weight = FontWeight.Bold),
    Font(resId = R.font.spoqahansansneo_light, weight = FontWeight.Light),
    Font(resId = R.font.spoqahansansneo_medium, weight = FontWeight.Medium),
    Font(resId = R.font.spoqahansansneo_thin, weight = FontWeight.Thin),
    )

fun getFontFamily(selection: Int): FontFamily{
    return when(selection){
        0 -> gothica1
        1 -> notosans
        2 -> maruburi
        3 -> spoqahans
        4 -> efdiary
        5 -> ongle_yunue
        6 -> ongle_eyeon
        else -> gothica1
    }
}