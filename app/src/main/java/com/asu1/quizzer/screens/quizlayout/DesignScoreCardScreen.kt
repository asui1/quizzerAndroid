package com.asu1.quizzer.screens.quizlayout

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.ColorPicker
import com.asu1.quizzer.composables.ScoreCardComposable
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.ShaderType
import com.asu1.quizzer.util.launchPhotoPicker
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import com.asu1.quizzer.viewModels.createSampleScoreCardViewModel
import kotlinx.coroutines.launch

@Composable
fun DesignScoreCardScreen(
    navController: NavController,
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
    scoreCardViewModel: ScoreCardViewModel = viewModel(),
    onUpload: () -> Unit = { }
) {
    val scoreCard by scoreCardViewModel.scoreCard.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current
    val photoPickerLauncher = launchPhotoPicker(context, screenWidth * 0.8f, screenHeight * 0.8f) { byteArray ->
        scoreCardViewModel.updateBackgroundImage(byteArray)
    }
    var showScoreCardColorPicker1 by remember { mutableStateOf(false) }
    var showScoreCardColorPicker2 by remember { mutableStateOf(false) }
    var showDropdownMenu by remember { mutableStateOf(false) }
    var showTextColorPicker by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    MaterialTheme(
        colorScheme = scoreCard.colorScheme
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if(showScoreCardColorPicker1){
                Dialog(
                    onDismissRequest = {
                        showScoreCardColorPicker1 = false
                    },
                    properties = DialogProperties(dismissOnBackPress = true)
                ) {
                    TextColorPickerModalSheet(
                        initialColor = scoreCard.background.color,
                        onColorSelected = { color ->
                            scoreCardViewModel.updateColor1(color)
                        },
                        text = stringResource(R.string.select_color1)
                    )
                }
            }
            if(showScoreCardColorPicker2){
                Dialog(
                    onDismissRequest = {
                        showScoreCardColorPicker2 = false
                    }
                ) {
                    TextColorPickerModalSheet(
                        initialColor = scoreCard.background.color2,
                        onColorSelected = { color ->
                            scoreCardViewModel.updateColor2(color)
                        },
                        text = stringResource(R.string.select_color2)
                    )
                }
            }
            if(showTextColorPicker){
                Dialog(
                    onDismissRequest = {
                        showTextColorPicker = false
                    }
                ) {
                    TextColorPickerModalSheet(
                        initialColor = scoreCard.textColor,
                        onColorSelected = { color ->
                            scoreCardViewModel.updateTextColor(color)
                        },
                        text = stringResource(R.string.select_text_color)
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
                ScoreCardComposable(
                    width = screenWidth * 0.8f,
                    height = screenHeight * 0.8f,
                    scoreCard = scoreCard,
                )
                Spacer(modifier = Modifier.height(8.dp))
                RowWithShares()
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        scope.launch {
                            quizLayoutViewModel.tryUpload(navController, scoreCard, onUpload)
                        }
                    },
                    modifier = Modifier
                        .size(width = screenWidth * 0.6f, height = 48.dp)
                        .padding(8.dp)
                        .testTag("DesignScoreCardUploadButton")
                ) {
                    Text(
                        text = stringResource(R.string.upload),
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
            IconButton(
                modifier = Modifier.testTag("DesignScoreCardSetTextColorButton"),
                onClick = {
                    showTextColorPicker = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.FormatColorText,
                    contentDescription = "Text Color",
                    modifier = Modifier.size(32.dp)
                )
            }
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
                    contentDescription = "Set background image",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                modifier = Modifier.testTag("DesignScoreCardSetColor1Button"),
                onClick = {
                    scoreCardViewModel.updateBackgroundState(ImageColorState.COLOR2)
                    showScoreCardColorPicker1 = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ColorLens,
                    contentDescription = "Set gradient color",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                modifier = Modifier.testTag("DesignScoreCardSetColor2Button"),
                onClick = {
                    scoreCardViewModel.updateBackgroundState(ImageColorState.COLOR2)
                    showScoreCardColorPicker2 = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ColorLens,
                    contentDescription = "Set gradient color",
                    modifier = Modifier.size(32.dp)
                )
            }
            Box(){
                IconButton(
                    modifier = Modifier.testTag("DesignScoreCardShaderButton"),
                    onClick = {
                        showDropdownMenu = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Animation,
                        contentDescription = "Set shader",
                        modifier = Modifier.size(32.dp)
                    )
                }
                DropdownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false },
                ) {
                    ShaderType.entries.forEach { shader ->
                        DropdownMenuItem(
                            modifier = Modifier.testTag("DesignScoreCardShaderButton${shader.index}"),
                            text = { Text(text = stringResource(shader.shaderName),
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
fun RowWithShares(
    onClickButton1: () -> Unit = { },
    onClickButton2: () -> Unit = { },
    onClickButton3: () -> Unit = { },
){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onClickButton1
        ) {
            Icon(
                imageVector = Icons.Default.Facebook,
                contentDescription = "Share Facebook",
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onClickButton2
        ) {
            Icon(
                painter = painterResource(id = R.drawable.x_logo),
                contentDescription = "Share X",
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onClickButton3
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share Link",
                modifier = Modifier.size(24.dp),
            )
        }
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
fun TextColorPickerModalSheet(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    text: String = "",
){
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ){
        Text(
            text = text ,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ColorPicker(
            initialColor = initialColor,
            onColorSelected = { color ->
                onColorSelected(color)
            },
            testTag = "DesignScoreCardTextColorPicker"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextColorPickerModalSheetPreview() {
    TextColorPickerModalSheet(
        initialColor = Color.Black,
        onColorSelected = { },
        text = "Select Text Color"
    )
}