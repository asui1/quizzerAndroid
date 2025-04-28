package com.asu1.quiz.content

import androidx.compose.ui.unit.dp

val dotSizeDp = 20.dp
val paddingDp = 4.dp
val boxPadding = 16.dp
val moveOffsetDp = (dotSizeDp + paddingDp * 2 - boxPadding) / 2
const val strokeWidth = 8f

enum class QuizMode { Viewer, Preview, Checker }

