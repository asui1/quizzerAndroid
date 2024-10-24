package com.asu1.quizzer.screens

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
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
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.ScoreCard
import com.asu1.quizzer.model.ShaderType
import com.asu1.quizzer.ui.theme.LightPrimary
import com.asu1.quizzer.ui.theme.LightSecondary
import com.asu1.quizzer.ui.theme.ongle_yunue
import com.asu1.quizzer.util.launchPhotoPicker
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.createSampleScoreCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
        scoreCardViewModel.updateBackgroundImage(byteArray)
    }
    var showScoreCardColorPicker by remember { mutableStateOf(false) }
    var showDropdownMenu by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = scoreCard.colorScheme
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if(showScoreCardColorPicker){
                ModalBottomSheet(
                    onDismissRequest = {
                        showScoreCardColorPicker = false
                    }
                ) {
                    ModalSheetForColorSelection(
                        colorScheme = scoreCard.colorScheme,
                        onColorSelected = { color ->
                            scoreCardViewModel.onColorSelection(color)
                        },
                        curSelection = scoreCard.background,
                    )
                }
            }
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
                        quizLayoutViewModel.tryUpload(navController, scoreCard)
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
                    scoreCardViewModel.updateBackgroundState(ImageColorState.IMAGE)
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
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
                    showScoreCardColorPicker = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Colorize,
                    contentDescription = "Close",
                    modifier = Modifier.size(32.dp)
                )
            }
            Box(){
                IconButton(
                    onClick = {
                        showDropdownMenu = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Animation,
                        contentDescription = "Close",
                        modifier = Modifier.size(32.dp)
                    )
                }
                DropdownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false },
                ) {
                    ShaderType.entries.forEach { shader ->
                        DropdownMenuItem(
                            text = { Text(text = shader.shaderName,
                                color = if (shader == scoreCard.shaderType) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            ) },
                            onClick = {
                                scoreCardViewModel.updateShaderType(shader)
                                showDropdownMenu = false
                            }
                        )
                    }
                }
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
                width = 3.dp,
                color = scoreCard.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Image(
            painter = ColorPainter(Color.White),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .then(scoreCard.background.asBackgroundModifierForScoreCard(
                    shaderOption = scoreCard.shaderType,
                    time = time
                ))

        )
        Text(
            text = scoreCard.score.toString(),
            style = TextStyle(
                fontFamily = ongle_yunue,
                fontSize = (400 * textSize).sp,
                color = scoreCard.colorScheme.error,
            ),
            modifier = Modifier
                .zIndex(2f)
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

        // Dot for resizing text
        Box(
            modifier = Modifier
                .zIndex(1f)
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

@Composable
fun ModalSheetForColorSelection(
    colorScheme: ColorScheme,
    onColorSelected: (Color) -> Unit = {},
    curSelection: ImageColor,
){
    val colors = listOf(
        colorScheme.primary,
        colorScheme.onPrimary,
        colorScheme.primaryContainer,
        colorScheme.onPrimaryContainer,
        colorScheme.secondary,
        colorScheme.onSecondary,
        colorScheme.secondaryContainer,
        colorScheme.onSecondaryContainer,
        colorScheme.tertiary,
        colorScheme.onTertiary,
        colorScheme.tertiaryContainer,
        colorScheme.onTertiaryContainer,
        colorScheme.error,
        colorScheme.onError,
        colorScheme.errorContainer
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(colors.size) { index ->
            val color = colors[index]
            Surface(
                shape = CircleShape,
                color = color,
                modifier = Modifier
                    .size(48.dp)
                    .border(
                        width = 5.dp,
                        color =
                        when(color){
                            curSelection.color -> LightPrimary
                            curSelection.color2 -> LightSecondary
                            else -> Color.Transparent
                        },
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(color) }
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ModalSheetForColorSelectionPreview() {
    ModalSheetForColorSelection(
        colorScheme = MaterialTheme.colorScheme,
        curSelection = ImageColor(
            color = MaterialTheme.colorScheme.primary,
            imageData = ByteArray(0),
            color2 = MaterialTheme.colorScheme.secondary,
            state = ImageColorState.COLOR2
        )
    )
}