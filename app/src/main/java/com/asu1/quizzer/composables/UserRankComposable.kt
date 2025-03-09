package com.asu1.quizzer.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.composables.animations.UserRankAnimation
import com.asu1.quizzer.screens.mainScreen.UriImageButton
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.userdatamodels.UserRank
import java.util.Locale
import kotlin.math.round

@Composable
fun UserRankComposable(
    userRank: UserRank,
    rank: Int
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenHeight = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.height.toDp() }
    }
    val screenWidth = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.width.toDp() }
    }
    val minSize = remember(screenWidth){
        minOf(minOf(screenWidth, screenHeight).times(0.2f), 200.dp)
    }
    val backgroundColor = when(rank%2){
        0 -> Color.Transparent
        1 -> MaterialTheme.colorScheme.surfaceContainerHigh
        else -> Color.Transparent
    }
    val average = remember(userRank.totalScore, userRank.quizzesSolved){String.format(Locale.US, "%.1f", round(userRank.totalScore / userRank.quizzesSolved * 10) / 10)}
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = when (rank) {
                    1 -> "\uD83E\uDD47"
                    2 -> "\uD83E\uDD48"
                    3 -> "\uD83E\uDD49"
                    else -> "${rank}."
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.width(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            UriImageButton(
                urlToImage = userRank.profileImageUri,
                modifier = Modifier
                    .size(minSize)
                    .clip(shape = RoundedCornerShape(16.dp)),
                nickname = userRank.nickname[0]
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(minSize)
            ) {
                Text(
                    text = userRank.nickname,
                    style = QuizzerTypographyDefaults.quizzerQuizCardTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(2f))
                Text(
                    text = buildString {
                        append(stringResource(R.string.points))
                        append(userRank.orderScore)
                    },
                    style = QuizzerTypographyDefaults.quizzerQuizCardCreator,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = buildString {
                        append(stringResource(R.string.average))
                        append(average)
                    },
                    style = QuizzerTypographyDefaults.quizzerQuizCardCreator,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = buildString {
                        append(stringResource(R.string.solved_quizzes))
                        append(userRank.quizzesSolved)
                    },
                    style = QuizzerTypographyDefaults.quizzerQuizCardCreator,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun UserRankComposableList(
    modifier: Modifier = Modifier,
    userRanks: List<UserRank>,
    getMoreUserRanks: () -> Unit = {},
) {
    val columnState = rememberLazyListState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItemIndex = columnState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= (userRanks.size - 1)
        }
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            getMoreUserRanks()
        }
    }

    LazyColumn(
        state = columnState,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(4.dp),
        modifier = modifier
    ) {
        item(key = "UserRankAnimation"){
            UserRankAnimation(
                modifier = Modifier.fillMaxWidth().height(100.dp)
            )
        }
        items(userRanks.size, key = {index -> userRanks[index].nickname}){index ->
            UserRankComposable(
                userRank = userRanks[index],
                rank = index + 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserRankComposablePreview() {
    val userRank = com.asu1.userdatamodels.userRankSample
    val userRanks = mutableListOf<com.asu1.userdatamodels.UserRank>()
    for (i in 1..20) {
        userRanks.add(
            userRank
        )
    }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(userRanks.size){index ->
            UserRankComposable(
                userRank = userRanks[index],
                rank = index + 1
            )
        }
    }
}