package com.asu1.quiz.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.sampleMultipleChoiceQuiz
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewer.BuildBody
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun MultipleChoiceQuizPreview(
    quiz: MultipleChoiceQuiz
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        QuestionTextStyle.GetTextComposable(
            quiz.question, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        BuildBody(
            quizBody = quiz.bodyValue,
        )
        quiz.userSelections.withIndex().forEach { (index, item) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Checkbox(
                    checked = item,
                    onCheckedChange = {
                    },
                    modifier = Modifier
                        .semantics {
                            contentDescription = "Checkbox at $index"
                        }
                        .scale(1.5f)
                )
                AnswerTextStyle.GetTextComposable(quiz.displayedOptions[index])
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMultipleChoiceQuizPreview(){
    QuizzerAndroidTheme {
        MultipleChoiceQuizPreview(
            sampleMultipleChoiceQuiz
        )
    }
}