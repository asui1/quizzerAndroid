package com.asu1.quizzer.composables.quizcards

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
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.util.constants.sampleQuizCardList


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun QuizCardHorizontal(quizCard: QuizCard,
                       onClick: () -> Unit = {},
                       onIconClick: (String) -> Unit = {},
                       modifier: Modifier = Modifier,
                       sharedTransitionScope: SharedTransitionScope,
                       animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = remember{configuration.screenWidthDp.dp}
    val screenHeight = remember{configuration.screenHeightDp.dp}
    val minSize = remember{minOf(screenWidth, screenHeight).times(0.30f).coerceAtMost(200.dp)}
    val borderColor = MaterialTheme.colorScheme.onSurface

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .clickable { onClick() }
            .height(minSize+2.dp)
            .width(screenWidth)
            .drawUnderBar(borderColor)
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
fun QuizCardHorizontal(quizCard: QuizCard,
                       onClick: (String) -> Unit = {},
                       modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = remember{configuration.screenWidthDp.dp}
    val screenHeight = remember{configuration.screenHeightDp.dp}
    val minSize = remember{minOf(screenWidth, screenHeight).times(0.35f).coerceAtMost(200.dp)}
    val borderColor = MaterialTheme.colorScheme.onSurface

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(minSize+2.dp)
            .width(screenWidth)
            .clickable { onClick(quizCard.id) }
            .drawUnderBar(borderColor)
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

fun Modifier.drawUnderBar(
    borderColor: Color
): Modifier{
    return this.then(Modifier.drawBehind {
        val strokeWidth = 1.dp.toPx()
        val y = size.height - strokeWidth / 2
        drawLine(
            color = borderColor,
            start = Offset(size.width * 0.05f, y),
            end = Offset(size.width * 0.95f, y),
            strokeWidth = strokeWidth
        )
    }
    )
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
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = quizCard.creator,
            style = MaterialTheme.typography.labelSmall,
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
            style = MaterialTheme.typography.bodySmall
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
            QuizCardHorizontal(quizCard, onClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCardHorizontalPreview() {
    val quizCardList = sampleQuizCardList
    QuizCardHorizontalList(quizCards = quizCardList)
}