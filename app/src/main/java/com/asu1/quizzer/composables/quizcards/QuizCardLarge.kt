package com.asu1.quizzer.composables.quizcards

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
import androidx.compose.foundation.pager.PageSize
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.composables.HorizontalPagerIndicator
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.getSampleQuizCard

@Composable
fun HorizontalQuizCardItemLarge(quizCards: List<QuizCard>, onClick: (String) -> Unit = {}) {

    val listState = rememberPagerState(
        initialPage = 0,
    ){
        quizCards.size
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        HorizontalPager(
            pageSize = object : PageSize {
                override fun Density.calculateMainAxisPageSize(
                    availableSpace: Int,
                    pageSpacing: Int
                ): Int {
                    return ((availableSpace - 2 * pageSpacing) * 0.95f).toInt()
                }
            },
            pageSpacing = 4.dp,
            state = listState,
            key = {index -> quizCards[index].id},
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        ) { page ->
            QuizCardLarge(quizCards[page], onClick)
        }
        HorizontalPagerIndicator(
            pageCount = quizCards.size,
            currentPage = listState.currentPage,
            targetPage = listState.targetPage,
            currentPageOffsetFraction = listState.currentPageOffsetFraction
        )
    }
}

@Composable
fun QuizCardLarge(quizCard: QuizCard, onClick: (String) -> Unit = {}, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenWidth = remember{configuration.screenWidthDp.dp}
    val screenHeight = remember{configuration.screenHeightDp.dp}
    val minSize = minOf(screenWidth, screenHeight).times(0.55f)
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
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
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier
                .padding(top = 4.dp)
                .height(minSize - 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = quizCard.title,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                )
                Text(
                    text = quizCard.creator,
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.weight(1f))
                TagsView(
                    tags = quizCard.tags,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .padding(horizontal = 4.dp),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.weight(1f))
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


@Preview(name = "QuizCardLarge Preview")
@Composable
fun QuizCardLargePreview() {
    val quizCard = getSampleQuizCard()

    HorizontalQuizCardItemLarge(quizCards = listOf(quizCard, quizCard, quizCard))
}

