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
        "quizzerTopBarTitle" to QuizzerTypographyDefaults.quizzerTopBarTitle,
        "quizzerIconLabel" to QuizzerTypographyDefaults.quizzerIconLabel,
        "quizTitleSmall" to QuizzerTypographyDefaults.quizzerQuizCardListTitle,
        "quizCreatorLarge" to QuizzerTypographyDefaults.quizzerQuizCardTitle,
        "quizCreatorMedium" to QuizzerTypographyDefaults.quizzerQuizCardCreator,
        "quizCreatorSmall" to QuizzerTypographyDefaults.quizzerQuizCardDescription,
        "quizTags" to QuizzerTypographyDefaults.quizzerQuizCardTags,
        "quizExplanation" to QuizzerTypographyDefaults.quizzerRoundTab,
        "quizzerUILarge" to QuizzerTypographyDefaults.quizzerUI,
        "quizzerUIMedium" to QuizzerTypographyDefaults.quizzerBodyMedium,
        "quizzerUISmall" to QuizzerTypographyDefaults.quizzerHeadlineMedium,
        "quizzerLabelLarge" to QuizzerTypographyDefaults.quizzerTitleMedium,
        "quizzerLabelMedium" to QuizzerTypographyDefaults.quizzerListItemTitle,
        "quizzerLabelSmall" to QuizzerTypographyDefaults.quizzerListItemSub,
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