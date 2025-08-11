package com.asu1.quizcard.cardBase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import kotlinx.collections.immutable.PersistentList

@Composable
fun VerticalQuizCardLarge(quizCard: QuizCard, onClick: (String) -> Unit = {}, index: Int) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenWidth = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.width.toDp() }
    }
    val screenHeight = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.height.toDp() }
    }
    val minSize = remember{minOf(screenWidth, screenHeight).times(0.35f).coerceAtMost(300.dp)}
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable { onClick(quizCard.id) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "${index+1}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
            VerticalQuizCardLargeBody(quizCard, minSize){
                QuizImage(
                    uuid = quizCard.id,
                    title = quizCard.title,
                    modifier = Modifier
                        .size(minSize)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
fun VerticalQuizCardLargeBody(
    quizCard: QuizCard,
    minSize: Dp,
    imageComposable: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ) {
        HeaderRow(
            minSize = minSize,
            imageComposable = imageComposable,
            quizCard = quizCard
        )
        Spacer(Modifier.height(8.dp))
        DescriptionText(quizCard.description)
    }
}

/* ---------------- header ---------------- */

@Composable
private fun HeaderRow(
    minSize: Dp,
    imageComposable: @Composable () -> Unit,
    quizCard: QuizCard
) {
    Row {
        imageComposable()
        Spacer(Modifier.width(8.dp))
        MetaColumn(minSize = minSize, quizCard = quizCard)
    }
}

@Composable
private fun MetaColumn(minSize: Dp, quizCard: QuizCard) {
    Column(
        modifier = Modifier.height(minSize),
        verticalArrangement = Arrangement.Center
    ) {
        TitleText(quizCard.title)
        Spacer(Modifier.weight(1f))
        CreatorText(quizCard.creator)
        Spacer(Modifier.weight(2f))
        TagsSection(quizCard.tags as PersistentList<String>)
        Spacer(Modifier.height(4.dp))
        Spacer(Modifier.weight(1f))
        SolvedCountText(quizCard.count)
        Spacer(Modifier.height(4.dp))
    }
}

/* ---------------- leaf pieces ---------------- */

@Composable
private fun TitleText(text: String) = Text(
    text = text,
    style = QuizzerTypographyDefaults.quizzerTitleSmallMedium,
    overflow = TextOverflow.Ellipsis,
    maxLines = 3
)

@Composable
private fun CreatorText(name: String) = Text(
    text = name,
    style = QuizzerTypographyDefaults.quizzerLabelSmallLight,
    overflow = TextOverflow.Ellipsis,
    fontWeight = FontWeight.Light,
    maxLines = 1
)

@Composable
private fun TagsSection(tags: PersistentList<String>) = TagsView(
    tags = tags,
    modifier = Modifier.fillMaxWidth().height(32.dp).padding(horizontal = 4.dp),
    maxLines = 3
)

@Composable
private fun SolvedCountText(count: Int) {
    val solvedText = stringResource(R.string.solved)
    val solved by remember(count) {
        mutableStateOf("${solvedText}$count")
    }
    Text(
        text = solved,
        style = QuizzerTypographyDefaults.quizzerLabelSmallLight
    )
}

@Composable
private fun DescriptionText(desc: String) = Text(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
    text = desc,
    style = QuizzerTypographyDefaults.quizzerBodySmall,
    minLines = 1,
    maxLines = 6
)

@Composable
fun VerticalQuizCardLargeColumn(
    modifier: Modifier = Modifier,
    quizCards: List<QuizCard>,
    onClick: (String) -> Unit = {},
    getMoreTrends: () -> Unit = {},
) {
    val columnState = rememberLazyListState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItemIndex = columnState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= (quizCards.size - 1)
        }
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            getMoreTrends()
        }
    }
    LazyColumn(
        state = columnState,
        contentPadding = PaddingValues(4.dp),
        modifier = modifier

    ) {
        items(quizCards.size,
            key = { index -> quizCards[index].id }
        ) { index ->
            VerticalQuizCardLarge(quizCards[index], onClick, index)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerticalQuizCardLargePreview() {
    val quizCards = sampleQuizCardList
    VerticalQuizCardLargeColumn(
        quizCards = quizCards,
    )
}

