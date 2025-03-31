package com.asu1.quizcard.cardBase

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun QuizCardHorizontalVerticalShare(
    quizCard: QuizCard, onLoadQuiz: (String) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope
) {
    var showDetails by remember{
        mutableStateOf(false)
    }

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
                sharedTransitionScope = sharedTransitionScope,
            )
        } else {
            QuizCardHorizontal(
                quizCard = quizCard,
                onClick = { showDetails = true },
                onIconClick = { onLoadQuiz(quizCard.id) },
                animatedVisibilityScope = this@AnimatedContent,
                sharedTransitionScope = sharedTransitionScope,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun QuizCardHorizontalVerticalShareList(
    quizCards: PersistentList<QuizCard>,
    onClick: (String) -> Unit = {}) {
    SharedTransitionLayout {
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
                    onLoadQuiz = onClick,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCardHorizontalVerticalShareListPreview() {
    val quizCards = sampleQuizCardList.toPersistentList()
    QuizCardHorizontalVerticalShareList(quizCards = quizCards)
}
