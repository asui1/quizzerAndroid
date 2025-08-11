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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.pageSelector.HorizontalPageIndicator
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList
import com.asu1.resources.QuizzerTypographyDefaults
import kotlinx.collections.immutable.PersistentList

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
    val minSize = rememberCardMinSquare()

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(quizCard.id) }
    ) {
        Row {
            QuizCardImage(
                uuid = quizCard.id,
                title = quizCard.title,
                size = minSize,
            )
            Spacer(Modifier.width(4.dp))
            QuizCardMeta(
                quizCard = quizCard,
                height = minSize - 4.dp,
            )
        }
    }
}

/* ---------- size & layout helpers ---------- */

const val WIDTH_HEIGHT_RATIO = 0.55f
@Composable
private fun rememberCardMinSquare(): Dp {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    return remember(windowInfo, density) {
        with(density) {
            val h = windowInfo.containerSize.height.toDp()
            val w = windowInfo.containerSize.width.toDp()
            minOf(w, h) * WIDTH_HEIGHT_RATIO
        }
    }
}

/* ---------- pieces ---------- */
@Composable
private fun QuizCardImage(
    uuid: String,
    title: String,
    size: Dp,
) {
    QuizImage(
        uuid = uuid,
        title = title,
        modifier = Modifier
            .size(size)               // 정사각형
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
private fun QuizCardMeta(
    quizCard: QuizCard,
    height: Dp,
) {
    Column(
        modifier = Modifier
            .padding(top = 4.dp)
            .height(height),
        verticalArrangement = Arrangement.Center
    ) {
        QuizCardTitle(quizCard.title)
        Spacer(Modifier.weight(1f))
        QuizCardCreator(quizCard.creator)
        Spacer(Modifier.weight(2f))
        QuizCardTags(quizCard.tags as PersistentList<String>)
        Spacer(Modifier.weight(2f))
        QuizCardDescription(quizCard.description)
    }
}

@Composable
private fun QuizCardTitle(text: String) {
    Text(
        text = text,
        style = QuizzerTypographyDefaults.quizzerTitleSmallMedium,
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
    )
}

@Composable
private fun QuizCardCreator(name: String) {
    Text(
        text = name,
        style = QuizzerTypographyDefaults.quizzerLabelSmallLight,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Composable
private fun QuizCardTags(tags: PersistentList<String>) {
    TagsView(
        tags = tags,
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .padding(horizontal = 4.dp),
        maxLines = 2
    )
}

@Composable
private fun QuizCardDescription(desc: String) {
    Text(
        text = desc,
        style = MaterialTheme.typography.bodySmall,
        minLines = 6,
        maxLines = 6,
    )
}

@Preview(showBackground = true)
@Composable
fun QuizCardLargePreview() {
    val quizCards = sampleQuizCardList

    HorizontalQuizCardItemLarge(quizCards = quizCards)
}

