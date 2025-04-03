package com.asu1.quiz.ui.textStyleManager

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.asu1.resources.LightColorScheme

object QuestionTextStyle : BaseTextStyleManager() {
    init {
        textStyle = TextStyle(fontSize = 24.sp)
        update(
            style = listOf(0, 0, 1, 0, 0),
            colorScheme = LightColorScheme,
            isDark = false,
        )
    }
}

object BodyTextStyle : BaseTextStyleManager() {
    init {
        textStyle = TextStyle(fontSize = 16.sp)
        update(
            style = listOf(0, 0, 2, 1, 0),
            colorScheme = LightColorScheme,
            isDark = false,
        )
    }
}

object AnswerTextStyle : BaseTextStyleManager() {
    init {
        textStyle = TextStyle(fontSize = 20.sp)
        update(
            style = listOf(0, 0, 0, 2, 0),
            colorScheme = LightColorScheme,
            isDark = false,
        )
    }
}