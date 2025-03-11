package com.asu1.custombuttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults

@Preview(showBackground = true)
@Composable
fun QuizzerTypographyPreview() {
    val typographyList = listOf(
        "quizzerTopBarTitle" to QuizzerTypographyDefaults.quizzerHeadlineSmallNormal,
        "quizzerIconLabel" to QuizzerTypographyDefaults.quizzerLabelSmallMedium,
        "quizTitleSmall" to QuizzerTypographyDefaults.quizzerTitleMediumBold,
        "quizCreatorLarge" to QuizzerTypographyDefaults.quizzerTitleSmallMedium,
        "quizCreatorMedium" to QuizzerTypographyDefaults.quizzerLabelSmallLight,
        "quizCreatorSmall" to QuizzerTypographyDefaults.quizzerBodySmallNormal,
        "quizTags" to QuizzerTypographyDefaults.quizzerBodySmallBold,
        "quizExplanation" to QuizzerTypographyDefaults.quizzerBodyMediumBold,
        "quizzerUILarge" to QuizzerTypographyDefaults.quizzerLabelMediumMedium,
        "quizzerUIMedium" to QuizzerTypographyDefaults.quizzerBodyMediumNormal,
        "quizzerUISmall" to QuizzerTypographyDefaults.quizzerHeadlineMediumBold,
        "quizzerLabelLarge" to QuizzerTypographyDefaults.quizzerTitleMediumMedium,
        "quizzerLabelMedium" to QuizzerTypographyDefaults.quizzerHeadlineSmallBold,
        "quizzerLabelSmall" to QuizzerTypographyDefaults.quizzerBodySmallLight,
        "quizzerBodySmall" to QuizzerTypographyDefaults.quizzerBodySmall,
    )
    QuizzerAndroidTheme {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
        ) {
            typographyList.forEach { (name, textStyle) ->
                Text(
                    text = "$name - 폰트 테스트?",
                    style = textStyle,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}