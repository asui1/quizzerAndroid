package com.asu1.quizzer.composables.quizcards

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.getSampleQuizCard

@Composable
fun QuizCardHorizontal(quizCard: QuizCard, onClick: (String) -> Unit = {}, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenWidth = remember{configuration.screenWidthDp.dp}
    val screenHeight = remember{configuration.screenHeightDp.dp}
    val minSize = remember{minOf(screenWidth, screenHeight).times(0.35f)}

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .wrapContentHeight()
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
            Column(modifier = Modifier
                .height(minSize)
                .padding(horizontal = 8.dp, vertical = 4.dp),
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
            QuizCardHorizontal(quizCard, onClick)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}

@Preview
@Composable
fun QuizCardHorizontalPreview() {
    val quizCard = getSampleQuizCard()
    QuizCardHorizontalList(quizCards = listOf(quizCard, quizCard, quizCard))
}