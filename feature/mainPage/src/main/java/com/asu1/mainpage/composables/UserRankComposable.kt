package com.asu1.mainpage.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.animations.UserRankAnimation
import com.asu1.mainpage.screens.mainScreen.UriImageButton
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.userdatamodels.UserRank

@Composable
fun UserRankComposable(
    userRank: UserRank,
    rank: Int
) {
    val screenInfo = rememberScreenInfo()
    val backgroundColor = if (rank % 2 == 1)
        MaterialTheme.colorScheme.surfaceContainerHigh
    else
        Color.Transparent

    RankCard(backgroundColor) {
        RankRow(
            rank = rank,
            profileUri = userRank.profileImageUri,
            nickname = userRank.nickname,
            orderScore = userRank.orderScore,
            totalScore = userRank.totalScore,
            quizzesSolved = userRank.quizzesSolved,
            minSize = screenInfo.minSize
        )
    }
}

// --- State holder for screen dimensions ---
@Immutable
private data class ScreenInfo(
    val width: Dp,
    val height: Dp,
    val minSize: Dp
)

@Composable
private fun rememberScreenInfo(): ScreenInfo {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val heightDp = with(density) { windowInfo.containerSize.height.toDp() }
    val widthDp = with(density) { windowInfo.containerSize.width.toDp() }
    val minSize = minOf((minOf(widthDp, heightDp) * 0.2f), 200.dp)
    return remember(widthDp, heightDp) { ScreenInfo(widthDp, heightDp, minSize) }
}

// --- Card container ---
@Composable
private fun RankCard(
    backgroundColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp), content = content)
    }
}

// --- Main row showing rank label, profile image, and user info ---
@Composable
private fun RankRow(
    rank: Int,
    profileUri: String,
    nickname: String,
    orderScore: Int,
    totalScore: Int,
    quizzesSolved: Int,
    minSize: Dp
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RankLabel(rank)
        Spacer(Modifier.width(8.dp))
        ProfileImage(uri = profileUri, initial = nickname.first(), size = minSize)
        Spacer(Modifier.width(8.dp))
        UserInfoColumn(
            nickname = nickname,
            orderScore = orderScore,
            totalScore = totalScore,
            quizzesSolved = quizzesSolved,
            height = minSize
        )
    }
}

@Composable
private fun RankLabel(rank: Int) {
    val label = when (rank) {
        1 -> "\uD83E\uDD47"
        2 -> "\uD83E\uDD48"
        3 -> "\uD83E\uDD49"
        else -> "$rank."
    }
    Text(
        text = label,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.width(40.dp)
    )
}

@Composable
private fun ProfileImage(
    uri: String,
    initial: Char,
    size: Dp
) {
    UriImageButton(
        urlToImage = uri,
        nickname = initial,
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Composable
private fun UserInfoColumn(
    nickname: String,
    orderScore: Int,
    totalScore: Int,
    quizzesSolved: Int,
    height: Dp
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Text(
            text = nickname,
            style = QuizzerTypographyDefaults.quizzerTitleSmallMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.weight(2f))
        Text(
            text = stringResource(R.string.points) + " $orderScore / $totalScore",
            style = QuizzerTypographyDefaults.quizzerLabelSmallLight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = stringResource(R.string.solved_quizzes) + " $quizzesSolved",
            style = QuizzerTypographyDefaults.quizzerLabelSmallLight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
    val userRanks = List(20) { com.asu1.userdatamodels.sampleUserRank }

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
