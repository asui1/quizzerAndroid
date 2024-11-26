package com.asu1.quizzer.composables.quizcards

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
import com.asu1.quizzer.model.getSampleQuizCard

@Composable
fun VerticalQuizCardLarge(quizCard: QuizCard, onClick: (String) -> Unit = {}, index: Int) {
    val configuration = LocalConfiguration.current
    val screenWidth = remember{configuration.screenWidthDp.dp}
    val screenHeight = remember{configuration.screenHeightDp.dp}
    val minSize = remember{minOf(screenWidth, screenHeight).times(0.4f)}
    val borderColor = MaterialTheme.colorScheme.onSurface
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable { onClick(quizCard.id) }
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = borderColor,
                    start = Offset(size.width * 0.05f, y),
                    end = Offset(size.width * 0.95f, y),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "${index+1}.",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ){
                Row(){
                    QuizImage(
                        uuid = quizCard.id,
                        title = quizCard.title,
                        modifier = Modifier
                            .size(minSize) // Square shape
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier
                        .height(minSize),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = quizCard.title,
                            style = MaterialTheme.typography.titleMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 3,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = quizCard.creator,
                            style = MaterialTheme.typography.labelMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 3,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TagsView(
                            tags = quizCard.tags,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .padding(horizontal = 4.dp),
                            maxLines = 3,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = quizCard.description,
                    style = MaterialTheme.typography.bodySmall,
                    minLines = 1,
                    maxLines = 6,
                )
            }
        }

    }
}

@Composable
fun VerticalQuizCardLargeColumn(quizCards: List<QuizCard>, onClick: (String) -> Unit = {},
                                modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(4.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(quizCards.size,
            key = { index -> quizCards[index].id }
        ) { index ->
            VerticalQuizCardLarge(quizCards[index], onClick, index)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerticalQuizCardLargePreview() {
    val quizCard = getSampleQuizCard()
    val quizCards = mutableListOf<QuizCard>()
    for (i in 1..3) {
        quizCards.add(
            quizCard
        )
    }
    VerticalQuizCardLargeColumn(quizCards, {})
}

