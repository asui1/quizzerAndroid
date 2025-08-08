package com.asu1.quiz.layoutBuilder.quizTextInputs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.QuizData
import com.asu1.resources.LayoutSteps

@Composable
fun QuizLayoutTitleDescriptionTag(
    quizData: QuizData = QuizData(),
    step: LayoutSteps = LayoutSteps.TITLE,
    proceed: () -> Unit = { -> },
    onTagToggle: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onTitleChange: (String) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        LazyColumn(
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Bottom),
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        ) {
            item("QuizTags") {
                AnimatedVisibility(
                    visible = step.value >= LayoutSteps.TAGS.value,
                ) {
                    QuizLayoutSetTags(
                        quizTags = quizData.tags,
                        onTagUpdate = { tag -> onTagToggle(tag) },
                        enabled = step == LayoutSteps.TAGS,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            item("QuizDescription") {
                AnimatedVisibility(
                    visible = step.value >= LayoutSteps.DESCRIPTION.value,
                ) {
                    QuizLayoutSetDescription(
                        quizDescription = quizData.description,
                        onDescriptionUpdate = { onDescriptionChange(it) },
                        proceed = { proceed() },
                        enabled = step == LayoutSteps.DESCRIPTION,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            item("QuizTitle") {
                QuizLayoutTitle(
                    title = quizData.title,
                    onTitleUpdate = { onTitleChange(it) },
                    proceed = { proceed() },
                    enabled = step == LayoutSteps.TITLE,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EnterRegisterInputDataPreview() {
    QuizLayoutTitleDescriptionTag(
        step = LayoutSteps.TAGS,
    )
}