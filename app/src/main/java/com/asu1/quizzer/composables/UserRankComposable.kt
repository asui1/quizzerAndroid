package com.asu1.quizzer.composables

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.model.UserRank
import com.asu1.quizzer.model.userRankSample
import com.asu1.quizzer.screens.mainScreen.UriImageButton
import com.asu1.quizzer.util.Logger
import java.util.Locale
import kotlin.math.round

@Composable
fun UserRankComposable(
    userRank: UserRank,
    rank: Int
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val minSize = minOf(screenWidth, screenHeight).times(0.3f)
//    1 -> Color(0xFFFFCC33)
    val backgroundColor = when(rank){
        1 -> Color(0xFFFFCC33)
        2 -> Color(0xFFBCC6CC)
        3 -> Color(0xFFCE8946)
        else -> Color.Transparent
    }
    Logger().debug(userRank.profileImageUri)
    val score = String.format(Locale.US, "%.1f", round(userRank.totalScore * 10) / 10)
    val average = String.format(Locale.US, "%.1f", round(userRank.totalScore / userRank.quizzesSolved * 10) / 10)
    Card(
        colors = if(backgroundColor != Color.Transparent) CardDefaults.cardColors(containerColor = Color.Transparent) else CardDefaults.cardColors(),
        modifier = Modifier
            .GlitteringBackground(baseColor = backgroundColor)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "${rank}.",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.width(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            UriImageButton(
                urlToImage = userRank.profileImageUri,
                modifier = Modifier
                    .size(minSize)
                    .clip(shape = RoundedCornerShape(8.dp)),
                nickname = userRank.nickname[0]
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ){
                Text(
                    text = userRank.nickname,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = buildString{
                        append(stringResource(R.string.points))
                        append(userRank.orderScore)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildString {
                        append(stringResource(R.string.average))
                        append(average)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildString {
                        append(stringResource(R.string.total_score))
                        append(score)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildString {
                        append(stringResource(R.string.solved_quizzes))
                        append(userRank.quizzesSolved)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
}

@Composable
fun UserRankComposableList(
    userRanks: List<UserRank>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(4.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(userRanks.size){index ->
            UserRankComposable(
                userRank = userRanks[index],
                rank = index + 1
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserRankComposablePreview() {
    val userRank = userRankSample
    UserRankComposableList(
        listOf(userRank, userRank, userRank, userRank, userRank)
    )
}

fun Modifier.GlitteringBackground(
    baseColor: Color,
): Modifier {
    if(baseColor == Color.Transparent) return this
    return this
        .graphicsLayer { alpha = 0.99f } // Enable hardware acceleration
        .background(
            shape = RoundedCornerShape(8.dp),
            brush = Brush.verticalGradient(
                colors = listOf(
                    baseColor.copy(alpha = 0.9f),
                    baseColor.copy(alpha = 0.6f),
                    baseColor.copy(alpha = 0.8f)
                )
            )
        )
        .drawWithCache {
            onDrawWithContent {
                drawContent()

                // Overlay with noise-like shimmer
                val glitterColor = baseColor.copy(alpha = 0.3f)
                val glitterPaint = Paint().apply {
                    shader = android.graphics.LinearGradient(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        glitterColor.toArgb(),
                        Color.White
                            .copy(alpha = 0.1f)
                            .toArgb(),
                        android.graphics.Shader.TileMode.MIRROR
                    )
                }

                drawRect(
                    brush = Brush.linearGradient(
                        listOf(glitterColor, Color.White),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height)
                    ),
                    alpha = 0.2f
                )
            }
        }
}
