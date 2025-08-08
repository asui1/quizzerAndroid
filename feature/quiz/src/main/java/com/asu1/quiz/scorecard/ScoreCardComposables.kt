package com.asu1.quiz.scorecard

import android.graphics.Bitmap
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.util.Locale

@Composable
fun ScoreCardBackground(
    backgroundImageColor: ImageColor,
    modifier: Modifier = Modifier,
) {
    val fill = Modifier.fillMaxSize()

    Box(modifier.then(fill)) {
        BackgroundLayer(backgroundImageColor, fill)
        EffectsLayer(backgroundImageColor)
        OverlayImageLayer(backgroundImageColor, fill)
    }
}

@Composable
private fun BackgroundLayer(bg: ImageColor, modifier: Modifier) {
    val baseResId = remember(bg.backgroundBase) { bg.backgroundBase.resourceId }
    val colorFilter = remember(bg.color, bg.imageBlendMode) {
        ColorFilter.tint(bg.color, blendMode = bg.imageBlendMode.blendMode)
    }

    when (bg.state) {
        ImageColorState.IMAGE -> ImageBitmapLayer(bg.imageData, colorFilter, modifier)
        ImageColorState.COLOR -> Image(
            painter = ColorPainter(bg.color),
            contentDescription = "ScoreCard Background",
            modifier = modifier
        )
        ImageColorState.BASEIMAGE -> Image(
            painter = painterResource(id = baseResId),
            colorFilter = colorFilter,
            contentDescription = "ScoreCard Background",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
        ImageColorState.GRADIENT -> GradientBrush(
            color = bg.color,
            color2 = bg.colorGradient,
            shaderType = bg.shaderType,
            modifier = modifier
        )
    }
}

@Composable
private fun ImageBitmapLayer(
    bytes: Bitmap,
    colorFilter: ColorFilter?,
    modifier: Modifier
) {
    // `remember` on the raw bytes; if you can provide a stable id/hash, prefer that
    val bitmap = remember(bytes) {
        bytes.asImageBitmap().apply { prepareToDraw() }
    }
    Image(
        bitmap = bitmap,
        colorFilter = colorFilter,
        contentDescription = "ScoreCard Background",
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
private fun EffectsLayer(bg: ImageColor) {
    if (bg.effect == Effect.NONE) return

    // Prefer stable defaults without allocation in composition
    val graphics = remember(bg.effect, bg.effectGraphics) {
        bg.effectGraphics.ifEmpty { bg.effect.defaultEffectGraphicsInfos }
    }
    EffectBuilder(
        color = bg.color2,
        resourceUrl = bg.effect.resourceUrl,
        blendModeCompat = bg.effect.blendMode,
        effectGraphicsInfos = graphics,
    )
}

@Composable
private fun OverlayImageLayer(bg: ImageColor, modifier: Modifier) {
    // Cheap guard to avoid decoding 1x1 (or empty) placeholder
    if (bg.overlayImage.width <= 1) return

    val overlayBitmap = remember(bg.overlayImage) {
        bg.overlayImage.asImageBitmap().apply { prepareToDraw() }
    }
    Image(
        bitmap = overlayBitmap,
        contentDescription = "ScoreCard Overlay",
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

const val PAGE_NUMBER = 4

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScoreCardComposable(
    modifier: Modifier = Modifier,
    scoreCard: ScoreCard,
    quizResult: QuizResult = sampleResult,
    pagerInit: Int = 0,
    quizQuestions: PersistentList<String> = persistentListOf()
) {
    // 1️⃣ State & derived colors
    val pagerState = rememberPagerState(initialPage = pagerInit, pageCount = { PAGE_NUMBER })
    val reddish    = remember(scoreCard.textColor) { rotateHue(scoreCard.textColor, -30f) }
    val greenish  = remember(scoreCard.textColor) { rotateHue(scoreCard.textColor,  30f) }
    val showIndicator by remember { derivedStateOf { pagerState.currentPage != PAGE_NUMBER - 1 } }

    ScoreCardContainer(modifier, scoreCard) {
        ScoreCardHeader(scoreCard, quizResult.nickname)
        ScoreCardPager(
            pagerState    = pagerState,
            textColor     = scoreCard.textColor,
            errorColor    = reddish,
            correctColor  = greenish,
            quizResult    = quizResult,
            quizQuestions = quizQuestions
        )
        if (showIndicator) {
            ScoreCardFooterIndicator(
                pagerState   = pagerState,
                indicatorColor = scoreCard.textColor
            )
        }
    }
}

@Composable
private fun ScoreCardContainer(
    modifier: Modifier,
    scoreCard: ScoreCard,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .animateContentSize()
            .fillMaxSize()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .aspectLimiter(maxRatio = 0.7f),
        contentAlignment = Alignment.BottomCenter
    ) {
        ScoreCardBackground(backgroundImageColor = scoreCard.background)
        content()
    }
}

private fun Modifier.aspectLimiter(maxRatio: Float): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val w = placeable.width
        val h = placeable.height
        val ratio = w.toFloat() / h
        if (ratio > maxRatio) {
            val newWidth = (h * maxRatio).toInt()
            layout(newWidth, h) { placeable.placeRelative(0, 0) }
        } else {
            layout(w, h) { placeable.placeRelative(0, 0) }
        }
    }
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ScoreCardHeader(
    scoreCard: ScoreCard,
    nickname: String
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement   = Arrangement.spacedBy(4.dp),
        horizontalAlignment   = Alignment.CenterHorizontally
    ) {
        Text(
            text      = scoreCard.title,
            color     = scoreCard.textColor,
            style     = MaterialTheme.typography.headlineSmallEmphasized,
            fontWeight= FontWeight.Black
        )
        Text(
            text      = nickname,
            color     = scoreCard.textColor,
            style     = MaterialTheme.typography.bodyMediumEmphasized,
            fontWeight= FontWeight.ExtraBold,
            modifier  = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun ScoreCardPager(
    pagerState: PagerState,
    textColor: Color,
    errorColor: Color,
    correctColor: Color,
    quizResult: QuizResult,
    quizQuestions: PersistentList<String>
) {
    HorizontalPager(
        state    = pagerState,
        key      = { it },
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) { page ->
        ScoreCardPage(
            page           = page,
            textColor      = textColor,
            errorColor     = errorColor,
            correctColor   = correctColor,
            quizResult     = quizResult,
            quizQuestions  = quizQuestions
        )
    }
}

@Composable
private fun ScoreCardPage(
    page: Int,
    textColor: Color,
    errorColor: Color,
    correctColor: Color,
    quizResult: QuizResult,
    quizQuestions: PersistentList<String>
) {
    // reuse your existing `cardItemWithSemiTransparentBackground` wrapper
    CardItemWithSemiTransparentBackground(
        textColor = textColor
    ) {
        when (page) {
            0 -> Score(
                scoreCardTextColor = textColor,
                correctQuestions   = quizResult.correction.count { it },
                totalQuestions     = quizResult.correction.size
            )
            1 -> AnswerCorrection(
                correction   = quizResult.correction,
                textColor    = textColor,
                errorColor   = errorColor,
                correctColor = correctColor,
                questions    = quizQuestions
            )
            2 -> PointDistribution(
                distribution      = quizResult.distribution,
                percent           = quizResult.percent,
                textColor         = textColor,
                selectedIndex     = quizResult.correction.count { it },
                selectedColor     = correctColor
            )
        }
    }
}

@Composable
private fun ScoreCardFooterIndicator(
    pagerState: PagerState,
    indicatorColor: Color
) {
    HorizontalPageIndicator(
        pageCount                       = PAGE_NUMBER,
        currentPage                     = pagerState.currentPage,
        currentPageOffsetFraction       = pagerState.currentPageOffsetFraction,
        modifier                        = Modifier.padding(bottom = 8.dp),
        indicatorColor                  = indicatorColor,
        targetPage = pagerState.targetPage,
    )
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