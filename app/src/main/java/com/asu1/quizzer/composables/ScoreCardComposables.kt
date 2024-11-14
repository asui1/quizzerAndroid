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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.asu1.quizzer.data.QuizResult
import com.asu1.quizzer.data.sampleResult
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.model.asBackgroundModifierForScoreCard
import com.asu1.quizzer.ui.theme.ongle_yunue
import com.asu1.quizzer.viewModels.createSampleScoreCardViewModel

@Composable
fun ScoreCardBackground(
    scoreCard: ScoreCard,
    time: Float
) {
    Image(
        painter = ColorPainter(Color.Transparent),
        contentDescription = "Background",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .asBackgroundModifierForScoreCard(
                imageColor = scoreCard.background,
                shaderOption = scoreCard.shaderType,
                time = time,
                )
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
    )
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

    val time by produceState(0f) {
        while(true){
            withInfiniteAnimationFrameMillis {
                value = it/1000f
            }
        }
    }
    val formattedScore = if (quizResult.score % 1 == 0f) {
        quizResult.score.toInt().toString()
    } else {
        quizResult.score.toString()
    }
    val redded = scoreCard.textColor.copy(
        red = (scoreCard.textColor.red + 0.5f).coerceAtMost(1f),
    )
    val greened = scoreCard.textColor.copy(
        blue = (scoreCard.textColor.blue + 0.5f).coerceAtMost(1f),
    )

    Box(
        modifier = Modifier
            .size(width = width, height = height)
    ) {
        ScoreCardBackground(
            scoreCard = scoreCard,
            time = time
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
                modifier = Modifier.fillMaxSize()
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
    val scoreCard by scoreCardViewModel.scoreCard.collectAsState()
    ScoreCardComposable(
        width = 300.dp,
        height = 600.dp,
        scoreCard = scoreCard,
    )
}

@Preview(showBackground = true)
@Composable
fun ScoreCardComposableAnswerCheckPreview() {
    val scoreCardViewModel = createSampleScoreCardViewModel()
    val scoreCard by scoreCardViewModel.scoreCard.collectAsState()
    ScoreCardComposable(
        width = 300.dp,
        height = 600.dp,
        scoreCard = scoreCard,
        pagerInit = 1,
    )
}

@Preview(showBackground = true)
@Composable
fun ScoreCardComposableGraphPreview() {
    val scoreCardViewModel = createSampleScoreCardViewModel()
    val scoreCard by scoreCardViewModel.scoreCard.collectAsState()
    ScoreCardComposable(
        width = 300.dp,
        height = 600.dp,
        scoreCard = scoreCard,
        pagerInit = 2,
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
    val formattedPercent = String.format("%.1f", percent)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentSize()
    ){
        Text("You are top ${formattedPercent}%!", color = textColor, style = MaterialTheme.typography.headlineSmall)
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
