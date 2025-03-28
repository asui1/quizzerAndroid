package com.asu1.quiz.scorecard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.asu1.customComposable.effects.EffectBuilder
import com.asu1.customComposable.effects.GradientBrush
import com.asu1.customComposable.pageSelector.HorizontalPageIndicator
import com.asu1.imagecolor.Effect
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.models.quiz.QuizResult
import com.asu1.models.quiz.sampleResult
import com.asu1.models.scorecard.ScoreCard
import com.asu1.models.scorecard.sampleScoreCard
import com.asu1.resources.NotoSans
import com.asu1.resources.R
import com.asu1.resources.robotoBlack
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.util.Locale

@Composable
fun ScoreCardBackground(
    backgroundImageColor: ImageColor,
    modifier: Modifier = Modifier,
) {
    val baseBackgroundResourceId = remember(backgroundImageColor.backgroundBase){
        backgroundImageColor.backgroundBase.resourceId
    }

    val colorFilter = remember(backgroundImageColor.color, backgroundImageColor.imageBlendMode){
        ColorFilter.tint(backgroundImageColor.color, blendMode = backgroundImageColor.imageBlendMode.blendMode)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        when(backgroundImageColor.state) {
            ImageColorState.IMAGE -> {
                val bitmap = remember(backgroundImageColor.imageData) {
                    backgroundImageColor.imageData.asImageBitmap().apply {
                        prepareToDraw()
                    }
                }
                Image(
                    bitmap = bitmap,
                    colorFilter = colorFilter,
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
                    colorFilter = colorFilter,
                    contentDescription = "ScoreCard Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            ImageColorState.GRADIENT -> {
                GradientBrush(
                    color = backgroundImageColor.color,
                    color2 = backgroundImageColor.colorGradient,
                    shaderType = backgroundImageColor.shaderType,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if(backgroundImageColor.effect != Effect.NONE){
            EffectBuilder(
                color = backgroundImageColor.color2,
                resourceUrl = backgroundImageColor.effect.resourceUrl,
                blendModeCompat = backgroundImageColor.effect.blendmode,
                contentScale = backgroundImageColor.effect.contentScale,
                effectGraphicsInfos =
                    backgroundImageColor.effectGraphics.ifEmpty {
                        backgroundImageColor.effect.defaultEffectGraphicsInfos
                    },
            )
        }
        if(backgroundImageColor.overlayImage.width > 1){
            val bitmap = remember(backgroundImageColor.overlayImage) {
                backgroundImageColor.overlayImage.asImageBitmap().apply {
                    prepareToDraw()
                }
            }
            Image(
                bitmap = bitmap,
                contentDescription = "ScoreCard Background",

                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .align(Alignment.BottomCenter)
            )
        }
    }
}


const val pageNum = 4

@Composable
fun ScoreCardComposable(
    modifier: Modifier = Modifier,
    scoreCard: ScoreCard,
    quizResult: QuizResult = sampleResult,
    pagerInit: Int = 0,
    quizQuestions: PersistentList<String> = persistentListOf<String>(),
){
    val pagerState = rememberPagerState(
        initialPage = pagerInit,
    ){
        pageNum
    }
    val redded = remember(scoreCard.textColor){
        rotateHue(scoreCard.textColor, -30f)
    }
    val greened = remember(scoreCard.textColor){
        rotateHue(scoreCard.textColor, 30f)
    }
    val showHorizontalIndicator by remember{
        derivedStateOf{
            pagerState.currentPage != 3
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .animateContentSize()
            .fillMaxSize()
            .clip(
                RoundedCornerShape(
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp,
                )
            )
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val width = placeable.width
                val height = placeable.height
                val ratio = width.toFloat() / height

                if (ratio > 0.7f) {
                    val newWidth = (height * 0.7f).toInt()
                    layout(newWidth, height) {
                        placeable.placeRelative(0, 0)
                    }
                } else {
                    layout(width, height) {
                        placeable.placeRelative(0, 0)
                    }
                }
            }
    ) {
        ScoreCardBackground(
            backgroundImageColor = scoreCard.background,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            cardItemWithSemiTransparentBackground(
                modifier = Modifier,
                textColor = scoreCard.textColor
            ) {
                Text(
                    text = scoreCard.title,
                    color = scoreCard.textColor,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    fontFamily = robotoBlack,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = quizResult.nickname,
                    color = scoreCard.textColor,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            HorizontalPager(
                state = pagerState,
                key = {it},
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent)
            ){ page ->
                if(page < 3) {
                    cardItemWithSemiTransparentBackground(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 8.dp),
                        textColor = scoreCard.textColor
                    ) {
                        when (page) {
                            1 -> {
                                AnswerCorrection(
                                    correction = quizResult.correction,
                                    textColor = scoreCard.textColor,
                                    errorColor = redded,
                                    correctColor = greened,
                                    questions = quizQuestions,
                                )
                            }
                            2 -> {
                                PointDistribution(
                                    distribution = quizResult.distribution,
                                    percent = quizResult.percent,
                                    textColor = scoreCard.textColor,
                                    selectedIndex = quizResult.correction.count { it },
                                    selectedColor = greened,
                                )
                            }
                            else -> {
                                Score(
                                    scoreCardTextColor = scoreCard.textColor,
                                    correctQuestions = quizResult.correction.count { it },
                                    totalQuestions = quizResult.correction.size,
                                )
                            }
                        }
                    }
                }
            }
        }
        AnimatedVisibility(visible = showHorizontalIndicator){
            HorizontalPageIndicator(
                pageCount = pageNum,
                currentPage = pagerState.currentPage,
                targetPage = pagerState.targetPage,
                currentPageOffsetFraction = pagerState.currentPageOffsetFraction,
                modifier = Modifier
                    .padding(bottom = 8.dp),
                indicatorColor = scoreCard.textColor,
            )
        }
    }
}

@Composable
private fun Score(
    scoreCardTextColor: Color,
    correctQuestions: Int,
    totalQuestions: Int,
) {
    val animatedScore = remember { Animatable(0f) }
    LaunchedEffect(correctQuestions) {
        animatedScore.animateTo(
            targetValue = correctQuestions.toFloat(),
            animationSpec = tween(durationMillis = 2000)
        )
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = stringResource(R.string.score_is),
            color = scoreCardTextColor,
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = NotoSans,
            maxLines = 1,
            modifier = Modifier
                .zIndex(2f),
        )
        Text(
            text = "${animatedScore.value.toInt()} / $totalQuestions",
            color = scoreCardTextColor,
            fontSize = 75.sp,
            fontFamily = NotoSans,
            maxLines = 1,
            modifier = Modifier
                .zIndex(2f),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScoreCardComposablePreview() {
    val scoreCard = sampleScoreCard
    ScoreCardComposable(
        scoreCard = scoreCard,
        pagerInit = 2,
    )
}

@Composable
fun AnswerCorrection(
    correction: PersistentList<Boolean> = listOf(true, true, false, false, true, false, true, false, true, false).toPersistentList(),
    textColor: Color,
    errorColor: Color,
    correctColor: Color,
    questions: PersistentList<String> = persistentListOf<String>()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()
    ) {
        items(correction.size, key = { index -> index }) { index ->
            if (questions.isNotEmpty() && index < questions.size) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${index + 1} :",
                        color = textColor,
                        maxLines = 1
                    )
                    Text(
                        text = questions[index],
                        color = textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (correction[index]) "O" else "X",
                        fontWeight = FontWeight.ExtraBold,
                        color = if (correction[index]) correctColor else errorColor,
                        maxLines = 1
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 75.dp)
                ) {
                    Box(modifier = Modifier.width(35.dp)) {
                        Text(
                            text = "${index + 1} :",
                            color = textColor,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = if (correction[index]) "O" else "X",
                        fontWeight = FontWeight.ExtraBold,
                        color = if (correction[index]) correctColor else errorColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun PointDistribution(
    distribution: PersistentList<Int>,
    percent: Float,
    textColor: Color,
    selectedIndex: Int,
    selectedColor: Color,
    height: Dp = 200.dp
){
    val formattedPercent = String.format(Locale.US, "%.1f", percent)

    GraphDrawer(
        modifier = Modifier.height(height).fillMaxWidth(),
        header = {
            Text(buildString {
                append(stringResource(R.string.you_are_top))
                append("${formattedPercent}%!")
            }, color = textColor, style = MaterialTheme.typography.headlineSmall)
        },
        distribution = distribution,
        textColor = textColor,
        selectedColor = selectedColor,
        selectedIndex = selectedIndex,
    )
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