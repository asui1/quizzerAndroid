package com.asu1.quizcard.cardBase

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun QuizCardHorizontal(
    modifier: Modifier = Modifier,
    quizCard: QuizCard,
    onClick: () -> Unit = {},
    onIconClick: (String) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenHeight = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.height.toDp() }
    }
    val screenWidth = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.width.toDp() }
    }
    val minSize = remember{minOf(screenWidth, screenHeight).times(0.30f).coerceAtMost(200.dp)}

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .clickable { onClick() }
            .height(minSize+2.dp)
            .width(screenWidth)
    ) {
        Row(modifier = Modifier) {
            with(sharedTransitionScope){
                QuizImage(
                    uuid = quizCard.id,
                    title = quizCard.title,
                    modifier = Modifier
                        .size(minSize)
                        .sharedElement(
                            rememberSharedContentState(key = quizCard.id),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                )
            }
            Box(
                modifier = Modifier
                    .height(minSize)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                QuizCardHorizontalTextBody(quizCard)
                IconButton(
                    onClick = { onIconClick(quizCard.id) },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Get Quiz Content",
                    )
                }
            }
        }
    }
}

@Composable
fun QuizCardHorizontal(
    modifier: Modifier = Modifier,
    quizCard: QuizCard,
    onClick: (String) -> Unit = {},
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenWidth = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.width.toDp() }
    }
    val screenHeight = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.height.toDp() }
    }
    val minSize = remember{minOf(screenWidth, screenHeight).times(0.35f).coerceAtMost(200.dp)}

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(minSize+2.dp)
            .width(screenWidth)
            .clickable { onClick(quizCard.id) }
    ) {
        Row(modifier = Modifier) {
            QuizImage(
                uuid = quizCard.id,
                title = quizCard.title,
                modifier = Modifier
                    .size(minSize) // Square shape
                    .clip(RoundedCornerShape(8.dp))
            )
            Box(
                modifier = Modifier
                    .height(minSize)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                QuizCardHorizontalTextBody(quizCard)
            }
        }
    }
}

@Composable
private fun QuizCardHorizontalTextBody(quizCard: QuizCard) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = quizCard.title,
            style = QuizzerTypographyDefaults.quizzerTitleSmallMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = quizCard.creator,
            style = QuizzerTypographyDefaults.quizzerBodySmallLight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        TagsView(
            tags = quizCard.tags,
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .padding(horizontal = 4.dp),
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = buildString {
                append(stringResource(R.string.solved))
                append(quizCard.count)
            },
            style = QuizzerTypographyDefaults.quizzerBodySmallLight,
        )
    }
}

@Composable
fun QuizCardHorizontalList(quizCards: List<QuizCard>, onClick: (String) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        items(quizCards,
            key = { quizCard -> quizCard.id }
        ) { quizCard ->
            QuizCardHorizontal(
                quizCard = quizCard,
                onClick = onClick,
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCardHorizontalPreview() {
    val quizCardList = sampleQuizCardList
    QuizCardHorizontalList(quizCards = quizCardList)
}