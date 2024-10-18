package com.asu1.quizzer.screens

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.ui.theme.ongle_yunue
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.customShader
import com.asu1.quizzer.util.launchPhotoPicker
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.createSampleScoreCardViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DesignScoreCardScreen(
    navController: NavController,
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
    scoreCardViewModel: ScoreCardViewModel = viewModel(),
    onUpload: () -> Unit = { }
) {
    //TODO ADD VIEWMODEL FOR SCORECARD
    val scoreCard by scoreCardViewModel.scoreCard.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current
    val photoPickerLauncher = launchPhotoPicker(context) { byteArray ->
    }

    MaterialTheme(
        colorScheme = scoreCard.colorScheme
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = scoreCard.title,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = scoreCard.creator,
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(modifier = Modifier.height(16.dp))
                ScoreCardComposable(
                    width = screenWidth * 0.7f,
                    height = screenHeight * 0.7f,
                    scoreCard = scoreCard,
                    onUpdateRatio = { x, y ->
                        scoreCardViewModel.updateRatio(x, y)
                    },
                    onUpdateSize = { size ->
                        scoreCardViewModel.updateSize(size)
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Solver",
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(modifier = Modifier.height(8.dp))
                RowWithShares()
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        /*TODO*/
                        // Save Quiz and navigate back to home when upload successful
                    },
                    modifier = Modifier
                        .size(width = screenWidth * 0.6f, height = 48.dp)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Upload",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize()
        ){
            //TODO
            // Buttons should have following contents.
            // 1. Set background as image
            // 2. Set background's color using primary/secondary/tertiary Colors.
            // 3. Change background's opengl state. SHADER USING AGSL
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.ImageSearch,
                    contentDescription = "Close",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                onClick = {
                    scoreCardViewModel.updateBackgroundState(ImageColorState.COLOR2)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Colorize,
                    contentDescription = "Close",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                onClick = {
                    scoreCardViewModel.updateBackgroundState(ImageColorState.COLOR2)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.InvertColors,
                    contentDescription = "Close",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }

}

@Composable
fun RowWithShares(){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Default.Facebook,
                contentDescription = "Share",
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.twitter),
                contentDescription = "Share",
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
fun ScoreCardComposable(
    width: Dp,
    height: Dp,
    scoreCard: ScoreCard,
    onUpdateRatio: (Float, Float) -> Unit,
    onUpdateSize: (Float) -> Unit,
){
    var xSize by remember { mutableStateOf(0.dp) }
    var ySize by remember { mutableStateOf(0.dp) }
    var textSize by remember {mutableStateOf(scoreCard.size)}
    var offsetX by remember { mutableStateOf(width * scoreCard.xRatio) }
    var offsetY by remember { mutableStateOf(height * scoreCard.yRatio) }
    var dotSize by remember { mutableStateOf(25.dp) }
    val density = LocalDensity.current

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
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp),
            )
            .then(scoreCard.background.asBackgroundModifierForScoreCard(
                shaderOption = scoreCard.shaderBrush,
                time = time
            ))
    ) {
        Text(
            text = scoreCard.score.toString(),
            style = TextStyle(
                fontFamily = ongle_yunue,
                fontSize = (400 * textSize).sp,
                color = scoreCard.colorScheme.error,
            ),
            modifier = Modifier
                .offset(x = offsetX - xSize/2, y = offsetY - ySize/2)
                .onGloballyPositioned { coordinates ->
                    xSize = density.run { coordinates.size.width.toDp() }
                    ySize = density.run { coordinates.size.height.toDp() }
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x.toDp()
                        offsetY += dragAmount.y.toDp()
                        onUpdateRatio(
                            offsetX.value / width.value,
                            offsetY.value / height.value
                        )
                    }
                }
        )

        Box(
            modifier = Modifier
                .offset(
                    x = offsetX + xSize/2 - dotSize / 2,
                    y = offsetY + ySize/2 - dotSize / 2
                )
                .size(dotSize)
                .background(scoreCard.colorScheme.error, CircleShape)
                .zIndex(1f)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val newSize = textSize + dragAmount.x/1000 + dragAmount.y/1000
                        textSize = newSize
                        onUpdateSize(textSize)
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DesignScoreCardPreview() {
    val scoreCardViewModel = createSampleScoreCardViewModel()
    DesignScoreCardScreen(
        navController = rememberNavController(),
        scoreCardViewModel = scoreCardViewModel,

        )
}

