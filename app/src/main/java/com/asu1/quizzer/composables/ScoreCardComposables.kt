package com.asu1.quizzer.composables

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asu1.quizzer.R
import com.asu1.quizzer.data.QuizResult
import com.asu1.quizzer.data.sampleResult
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.ui.theme.ongle_yunue
import com.asu1.quizzer.viewModels.createSampleScoreCardViewModel
import com.materialkolor.ktx.darken
import com.materialkolor.ktx.lighten
import java.util.Locale
import kotlin.math.round

@Composable
fun ScoreCardBackground(
    scoreCard: ScoreCard,
    width: Dp,
    height: Dp,
    time: Float = 0f,
    modifier: Modifier = Modifier,
) {
    val colorMatrix1 = remember(scoreCard.background.color) {
        createBlendingColorMatrix(scoreCard.background.color)
    }
    val colorMatrix2 = remember(scoreCard.background.color2) {
        createBlendingColorMatrix(scoreCard.background.color2)
    }
    val density = LocalDensity.current
    val imageWidthPx = remember(density){with(density) { width.toPx() }}
    val xVal1 = ((time * 100) % imageWidthPx) * 2 - imageWidthPx
    val xVal2 = (((time * 1.1f) * 100) % imageWidthPx) * 2 - imageWidthPx
    val xVal3 = (((time * 1.3f) * 100) % imageWidthPx) * 2 - imageWidthPx
    val imagePositions = listOf(xVal1, xVal2, xVal3)

    // 배경 이미지를 설정하고, 그 위를 지나가는 구름을 랜덤하게 만드는 방향으로 해야겠다. Canvas 설정하고 그 위에 구름을 그리는 방식으로
    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.sky_background),
            colorFilter = ColorFilter.colorMatrix(colorMatrix1),
            contentDescription = stringResource(R.string.background),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        imagePositions.forEachIndexed { index, position ->
            Image(
                painter = painterResource(id = R.drawable.single_cloud_nobackground),
                colorFilter = ColorFilter.colorMatrix(colorMatrix2),
                contentDescription = stringResource(R.string.background),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = position
                        translationY = when(index){
                            0 -> 0f
                            1 -> imageWidthPx/4
                            else -> imageWidthPx/2
                        }

                    }
                    .size(width)
            )
        }
    }
}

fun createBlendingColorMatrix(color: Color): ColorMatrix {

    val r = color.red
    val g = color.green
    val b = color.blue

    // Create a ColorMatrix that applies the given color
    val matrix = ColorMatrix(floatArrayOf(
        r, 0f, 0f, 0f, 0f,
        0f, g, 0f, 0f, 0f,
        0f, 0f, b, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    ))
    return matrix
}

@Composable
fun ScoreCardComposable(
    width: Dp,
    height: Dp,
    scoreCard: ScoreCard,
    quizResult: QuizResult = sampleResult,
    pagerInit: Int = 0,
){
    val pagerState = rememberPagerState(
        initialPage = pagerInit,
    ){
        3
    }

    val formattedScore = if (quizResult.score % 1 == 0f) {
        quizResult.score.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", round(quizResult.score * 10) / 10)
    }
    val redded = scoreCard.textColor.copy(
        red = (scoreCard.textColor.red + 0.5f).coerceAtMost(1f),
    )
    val greened = scoreCard.textColor.copy(
        blue = (scoreCard.textColor.blue + 0.5f).coerceAtMost(1f),
    )
    val time by produceState(0f) {
        while(true){
            withInfiniteAnimationFrameMillis {
                value = it/1000f
            }
        }
    }
    Box(
        modifier = Modifier
            .size(width = width, height = height)
    ) {
        ScoreCardBackground(
            scoreCard = scoreCard,
            width = width,
            height = height,
            time = time,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = scoreCard.title,
                color = scoreCard.textColor,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = quizResult.nickname,
                color = scoreCard.textColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.End)
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent)
            ){ page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    when(page) {
                        1 -> {
                            AnswerCorrection(
                                correction = quizResult.correction,
                                textColor = scoreCard.textColor,
                                errorColor = redded,
                                correctColor = greened,
                            )
                        }
                        2 -> {
                            PointDistribution(
                                distribution = quizResult.distribution,
                                percent = quizResult.percent,
                                score = quizResult.score,
                                textColor = scoreCard.textColor,
                                errorColor = redded,
                            )
                        }
                        else -> {
                            Text(
                                text = formattedScore,
                                style = TextStyle(
                                    fontFamily = ongle_yunue,
                                    fontSize = 200.sp,
                                    color = scoreCard.textColor,
                                ),
                                modifier = Modifier
                                    .zIndex(2f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScoreCardComposablePreview() {
    val scoreCardViewModel = createSampleScoreCardViewModel()
    val scoreCard by scoreCardViewModel.scoreCard.collectAsStateWithLifecycle()
    ScoreCardComposable(
        width = 300.dp,
        height = 600.dp,
        scoreCard = scoreCard,
    )
}

@Composable
fun AnswerCorrection(
    correction: List<Boolean> = listOf(true, true, false, false, true, false, true, false, true, false),
    textColor: Color,
    errorColor: Color,
    correctColor: Color,
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()
    ) {
        items(correction.size) { index ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(modifier = Modifier.width(35.dp)) {
                    Text(
                        text = "${index + 1} : ",
                        color = textColor,
                        maxLines = 1,
                    )
                }
                Text(
                    text = if (correction[index]) "O" else "X",
                    fontWeight = FontWeight.ExtraBold,
                    color = if (correction[index]) correctColor else errorColor
                )
            }
        }
    }

}

@Composable
fun PointDistribution(
    distribution: List<Int>,
    percent: Float,
    score: Float,
    textColor: Color,
    errorColor: Color,
    height: Dp = 200.dp
){
    val maxItem = distribution.maxOrNull() ?: 1
    val formattedPercent = String.format(Locale.US, "%.1f", percent)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentSize()
    ){
        Text(buildString {
            append(stringResource(R.string.you_are_top))
            append("${formattedPercent}%!")
        }, color = textColor, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            distribution.forEachIndexed { index, value ->
                val color = if ((index * 5).toFloat() <= score && score < ((index + 1) * 5).toFloat()) errorColor else textColor
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(height * value / maxItem + 20.dp)
                        .background(color)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
        ){
            for(i in listOf("0", "50", "100")){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(30.dp)
                ) {
                    Text(
                        text = i,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
