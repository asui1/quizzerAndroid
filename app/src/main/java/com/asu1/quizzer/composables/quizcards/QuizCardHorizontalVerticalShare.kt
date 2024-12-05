package com.asu1.quizzer.composables.quizcards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.getSampleQuizCard
import com.asu1.quizzer.model.getSampleQuizCardList

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun QuizCardHorizontalVerticalShare(quizCard: QuizCard, onLoadQuiz: (String) -> Unit = {}) {
    var showDetails by remember{
        mutableStateOf(false)
    }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = showDetails,
            label = "QuizCard content with or without details"
        ) {targetState ->
            if (targetState) {
                VerticalQuizCardLargeShare(
                    quizCard = quizCard,
                    onClick = { showDetails = false },
                    onIconClick = { onLoadQuiz(quizCard.id) },
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    )
            } else {
                QuizCardHorizontal(
                    quizCard = quizCard,
                    onClick = { showDetails = true },
                    onIconClick = { onLoadQuiz(quizCard.id) },
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout,
                )
            }
        }
    }
}

@Composable
fun QuizCardHorizontalVerticalShareList(quizCards: List<QuizCard>, onClick: (String) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        items(quizCards,
            key = { quizCard -> quizCard.id }
        ) { quizCard ->
            QuizCardHorizontalVerticalShare(
                quizCard = quizCard,
                onLoadQuiz = onClick
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCardHorizontalVerticalShareListPreview() {
    val quizCards = getSampleQuizCardList()
    QuizCardHorizontalVerticalShareList(quizCards = quizCards)
}
