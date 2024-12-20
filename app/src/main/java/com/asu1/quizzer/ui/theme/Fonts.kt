package com.asu1.quizzer.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.asu1.quizzer.R

val efdiary = FontFamily(
    Font(R.font.ef_diary),
)
val gothica1 = FontFamily(
    Font(resId = R.font.gothica1_bold, weight = FontWeight.Bold),
    Font(resId = R.font.gothica1_regular, weight = FontWeight.Normal),
    Font(resId = R.font.gothica1_light, weight = FontWeight.Light),
)

val maruburi = FontFamily(
    Font(resId = R.font.maruburi, weight = FontWeight.Normal),
    Font(resId = R.font.maruburi_bold, weight = FontWeight.Bold),
    Font(resId = R.font.maruburi_light, weight = FontWeight.Light),
)
val notosans = FontFamily(
    Font(resId = R.font.notosanskr_regular, weight = FontWeight.Normal),
    Font(resId = R.font.notosanskr_light, weight = FontWeight.Light),
    Font(resId = R.font.notosanskr_bold, weight = FontWeight.Bold),
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