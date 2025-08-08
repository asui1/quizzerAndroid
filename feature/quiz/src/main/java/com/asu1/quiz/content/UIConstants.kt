package com.asu1.quiz.content

import androidx.compose.ui.unit.dp

val DOT_SIZE_DP = 20.dp
val PADDING_SIZE_DP = 4.dp
val BOX_PADDING = 16.dp
val MOVE_OFFSET_DP = (DOT_SIZE_DP + PADDING_SIZE_DP * 2 - BOX_PADDING) / 2
const val STROKE_WIDTH = 8f

enum class QuizMode { Viewer, Preview, Checker }

