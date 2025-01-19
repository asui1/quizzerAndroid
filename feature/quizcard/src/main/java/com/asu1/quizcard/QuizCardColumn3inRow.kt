package com.asu1.quizcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCardList

@Composable
fun HorizontalQuizCardItemVertical(quizCards: List<QuizCard>, onClick: (String) -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        quizCards.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                rowItems.forEach { quizCard ->
                    QuizCardItemVertical(
                        quizCard = quizCard,
                        onClick = onClick,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size < 3) {
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun QuizCardItemVertical(quizCard: QuizCard,
                         onClick: (String) -> Unit = {},
                         modifier:Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = remember{configuration.screenWidthDp.dp / 3}

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .wrapContentHeight()
            .padding(horizontal = 4.dp)
            .clickable { onClick(quizCard.id) }
    ) {
        Column(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth())
        {
            QuizImage(
                uuid = quizCard.id,
                title = quizCard.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenWidth)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
                text = quizCard.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                minLines = 2,
                maxLines = 2,
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = quizCard.creator,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun QuizCardHorizontalWithSharedTransition(

){

}

@Preview(name = "QuizCardItemVertical Preview", showBackground = true)
@Composable
fun QuizCardItemPreview() {
    val quizCardList = sampleQuizCardList

    HorizontalQuizCardItemVertical(quizCards = quizCardList)
}

