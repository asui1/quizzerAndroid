package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.sampleQuizList
import com.asu1.quiz.content.quizCommonBuilder.QuizPreview
import com.asu1.quiz.ui.ImageColorBackground
import com.asu1.resources.QuizzerAndroidTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun sampleQuizFlow(): Flow<Quiz> = flow {
    for (quiz in sampleQuizList) {
        emit(quiz)
        delay(400L)
    }
}


@Composable
fun QuizThemeExample(
    modifier: Modifier = Modifier,
    quizTheme: QuizTheme,
){
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenWidth = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.width.toDp() * 0.95f }
    }
    val screenHeight = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.height.toDp().coerceAtMost(800.dp)}
    }
    val scale = 0.3f
    val sampleQuizList = remember { mutableStateListOf<Quiz>() }

    LaunchedEffect(Unit) {
        sampleQuizFlow().collect { quiz ->
            sampleQuizList.add(quiz)
        }
    }

    MaterialTheme(
        colorScheme = quizTheme.colorScheme
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .heightIn(screenHeight * scale)
        ) {
            items(sampleQuizList, key = {it.uuid}) { quiz ->
                Box(
                    modifier = Modifier
                        .size(width = screenWidth * scale, height = screenHeight * scale)
                        .requiredSize(width = screenWidth, height = screenHeight)
                        .scale(scale)
                        .border(1.dp,
                            MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(5),
                        )
                ) {
                    ImageColorBackground(imageColor = quizTheme.backgroundImage)
                    QuizPreview(
                        quiz
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizThemeExample(){
    QuizzerAndroidTheme {
        QuizThemeExample(
            quizTheme = QuizTheme(),
        )
    }
}
