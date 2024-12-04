package com.asu1.quizzer.composables.scorecard

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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.effects.Clouds
import com.asu1.quizzer.composables.effects.Fireworks
import com.asu1.quizzer.composables.effects.GradientBrush
import com.asu1.quizzer.composables.effects.Snowflake
import com.asu1.quizzer.composables.effects.WithFlowers
import com.asu1.quizzer.composables.effects.WithMoon
import com.asu1.quizzer.composables.effects.WithShootingStar
import com.asu1.quizzer.data.QuizResult
import com.asu1.quizzer.data.sampleResult
import com.asu1.quizzer.model.Effect
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.ui.theme.ongle_yunue
import com.asu1.quizzer.viewModels.createSampleScoreCardViewModel
import java.util.Locale
import kotlin.math.round

@Composable
fun ScoreCardBackground(
    backgroundImageColor: ImageColor,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val colorMatrix1 = remember(backgroundImageColor.color) {
        if(backgroundImageColor.color != Color.Transparent) {
            createBlendingColorMatrix(backgroundImageColor.color)
        } else {
            ColorMatrix()
        }
    }
    val baseBackgroundResourceId = remember(backgroundImageColor.backgroundBase){
        backgroundImageColor.backgroundBase.resourceId
    }

    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(RoundedCornerShape(16.dp))
    ) {
        when(backgroundImageColor.state) {
            ImageColorState.IMAGE -> {
                Image(
                    painter = remember(backgroundImageColor.imageData) { backgroundImageColor.getAsImage() },
                    colorFilter = ColorFilter.colorMatrix(colorMatrix1),
                    contentDescription = "ScoreCard Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            ImageColorState.COLOR -> {
                Image(
                    painter = ColorPainter(backgroundImageColor.color),
                    contentDescription = "ScoreCard Background",
                    modifier = Modifier.fillMaxSize()
                )
            }
            ImageColorState.BASEIMAGE -> {
                Image(
                    painter = painterResource(id = baseBackgroundResourceId),
                    colorFilter = ColorFilter.colorMatrix(colorMatrix1),
                    contentDescription = "ScoreCard Background",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
                )
            }
            ImageColorState.GRADIENT -> {
                GradientBrush(
                    imageColor = backgroundImageColor,
                    shaderType = backgroundImageColor.shaderType,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        when(backgroundImageColor.effect) {
            Effect.FIREWORKS->{
                Fireworks(
                    color = backgroundImageColor.color2,
                    rawResource = R.raw.firework,
                )
            }
            Effect.MOON->{
                WithMoon(
                    color = backgroundImageColor.color2,
                    rawResource = R.raw.moon,
                )
            }
            Effect.SHOOTING_STAR-> {
                WithShootingStar(
                    color = backgroundImageColor.color2,
                    rawResource = R.raw.shootingstar,
                )
            }
            Effect.CLOUDS->{
                Clouds(
                    color = backgroundImageColor.color2,
                    rawResource = R.raw.clouds,
                )
            }
            Effect.SNOWFLAKES ->{
                Snowflake(
                    color = backgroundImageColor.color2,
                    rawResource = R.raw.snowfalling,
                )
            }
            Effect.FLOWERS ->{
                WithFlowers(
                    color = backgroundImageColor.color2,
                    rawResource = R.raw.flowers,
                )
            }
            else ->{
            }
        }

    }
}


fun createBlendingColorMatrix(color: Color): ColorMatrix {

    val r = color.red
    val g = color.green
    val b = color.blue
    val a = color.alpha

    // Create a ColorMatrix that applies the given color
    val matrix = ColorMatrix(floatArrayOf(
        r, 0f, 0f, 0f, 0f,
        0f, g, 0f, 0f, 0f,
        0f, 0f, b, 0f, 0f,
        0f, 0f, 0f, a, 0f
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

    val formattedScore = remember(quizResult.score){
        if (quizResult.score % 1 == 0f) {
            quizResult.score.toInt().toString()
        } else {
            String.format(Locale.US, "%.1f", round(quizResult.score * 10) / 10)
        }
    }
    val redded = remember(scoreCard.textColor){
        rotateHue(scoreCard.textColor, -30f)
    }
    val greened = remember(scoreCard.textColor){
        rotateHue(scoreCard.textColor, 30f)
    }

    Box(
        modifier = Modifier
            .size(width = width, height = height)
    ) {
        ScoreCardBackground(
            backgroundImageColor = scoreCard.background,
            width = width,
            height = height,
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
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            ) {
                                Text(
                                    text = formattedScore,
                                    style = TextStyle(
                                        fontFamily = ongle_yunue,
                                        fontSize = 200.sp,
                                        color = scoreCard.textColor,
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .zIndex(2f)
                                )
                            }
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
                val isSelected = (index * 5).toFloat() <= score && score < ((index + 1) * 5).toFloat()
                val color = if (isSelected) errorColor else textColor
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
                        color = textColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

fun rotateHue(color: Color, degrees: Float): Color {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(color.toArgb(), hsv)

    // Rotate the hue
    hsv[0] = (hsv[0] + degrees) % 360

    // Ensure saturation and brightness are not zero
    if (hsv[1] == 0f) hsv[1] = 0.5f
    if (hsv[2] == 0f) hsv[2] = 0.5f

    return Color(android.graphics.Color.HSVToColor(hsv))
}