package com.asu1.quizcard.cardBase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.pageSelector.HorizontalPageIndicator
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList
import com.asu1.resources.QuizzerTypographyDefaults

@Composable
fun HorizontalQuizCardItemLarge(
    modifier: Modifier = Modifier,
    quizCards: List<QuizCard>, onClick: (String) -> Unit = {}) {

    val listState = rememberPagerState(
        initialPage = 0,
    ){
        quizCards.size
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        HorizontalPager(
            pageSpacing = 4.dp,
            state = listState,
            key = {index -> quizCards[index].id},
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) { page ->
            QuizCardLarge(quizCard = quizCards[page], onClick = onClick)
        }
        HorizontalPageIndicator(
            pageCount = quizCards.size,
            currentPage = listState.currentPage,
            targetPage = listState.targetPage,
            currentPageOffsetFraction = listState.currentPageOffsetFraction
        )
    }
}

@Composable
fun QuizCardLarge(
    modifier: Modifier = Modifier,
    quizCard: QuizCard,
    onClick: (String) -> Unit = {},
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenHeight = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.height.toDp() }
    }
    val screenWidth = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.width.toDp() }
    }
    val minSize = minOf(screenWidth, screenHeight).times(0.55f)
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(4.dp)
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
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier
                .padding(top = 4.dp)
                .height(minSize - 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = quizCard.title,
                    style = QuizzerTypographyDefaults.quizzerTitleSmallMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = quizCard.creator,
                    style = QuizzerTypographyDefaults.quizzerLabelSmallLight,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.weight(2f))
                TagsView(
                    tags = quizCard.tags,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .padding(horizontal = 4.dp),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.weight(2f))
                Text(
                    text = quizCard.description,
                    style = MaterialTheme.typography.bodySmall,
                    minLines = 6,
                    maxLines = 6,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun QuizCardLargePreview() {
    val quizCards = sampleQuizCardList

    HorizontalQuizCardItemLarge(quizCards = quizCards)
}

