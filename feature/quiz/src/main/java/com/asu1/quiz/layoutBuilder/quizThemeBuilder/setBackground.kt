package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import android.graphics.Bitmap
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.colorPicker.ColorPicker
import com.asu1.customComposable.imageGetter.ImageGetter
import com.asu1.imagecolor.ImageColor
import com.asu1.imagecolor.ImageColorState
import com.asu1.quiz.viewmodel.quizLayout.QuizThemeActions
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R

@Composable
fun QuizLayoutSetBackground(
    modifier: Modifier = Modifier,
    backgroundImageColor: ImageColor,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
    scrollTo: () -> Unit = {}
) {
    var isOpen by remember { mutableStateOf(false) }
    var selectedTabIndex by
    remember { mutableIntStateOf(backgroundImageColor.state.ordinal) }

    // Keep tab in sync with incoming state
    LaunchedEffect(backgroundImageColor.state) {
        selectedTabIndex = when (backgroundImageColor.state) {
            ImageColorState.COLOR    -> 0
            ImageColorState.GRADIENT -> 1
            ImageColorState.IMAGE    -> 2
            else                     -> 0
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackgroundSectionHeader()
        BackgroundTabRow(
            selectedIndex = selectedTabIndex,
            onTabSelected = { idx, same ->
                selectedTabIndex = idx
                isOpen = same.not() || !isOpen
                if (isOpen) scrollTo()
            }
        )
        if (isOpen) {
            BackgroundContent(
                selectedTab = selectedTabIndex,
                background   = backgroundImageColor,
                updateTheme  = updateQuizTheme
            )
        }
    }
}

@Composable
private fun BackgroundSectionHeader() {
    Text(
        text       = stringResource(R.string.background),
        fontWeight = FontWeight.ExtraBold,
        modifier   = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun BackgroundTabRow(
    selectedIndex: Int,
    onTabSelected: (index: Int, clickedSame: Boolean) -> Unit
) {
    BackgroundSecondaryTabRow(
        selectedTabIndex = selectedIndex,
        updateQuizTheme  = {}, // already handled upstream
        onClick = { clickedSame ->
            onTabSelected(selectedIndex, clickedSame)
        }
    )
}

@Composable
private fun BackgroundContent(
    selectedTab: Int,
    background:  ImageColor,
    updateTheme: (QuizThemeActions) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        when (selectedTab) {
            0 ->  {
                Spacer(modifier = Modifier.height(4.dp))
                ColorPicker(
                    initialColor = background.color,
                    colorName = stringResource(R.string.background),
                    onColorSelected = { color ->
                        updateTheme(
                            QuizThemeActions.UpdateBackgroundColor(color)
                        )
                    }
                )
            }
            1 ->                         SetGradientBackground(
                backgroundImageColor = background,
                updateQuizTheme = updateTheme,
            )
            2 -> ImageBackgroundPicker(
                image       = background.imageData,
                onImagePick = { bmp -> updateTheme(QuizThemeActions.UpdateBackgroundImage(bmp)) }
            )
        }
    }
}

@Composable
private fun ImageBackgroundPicker(
    image: Bitmap,
    onImagePick: (Bitmap) -> Unit
) {
    ImageGetter(
        image         = image,
        onImageUpdate = onImagePick
    )
}

@Composable
fun BackgroundSecondaryTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
    onClick: (clickedSame: Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
        ) {
            Tab(selected = selectedTabIndex == 0, onClick = {
                onClick(selectedTabIndex == 0)
                updateQuizTheme(
                    QuizThemeActions.UpdateBackgroundType(ImageColorState.COLOR)
                )
            }) {
                Text(
                    stringResource(R.string.single_color),
                    style = QuizzerTypographyDefaults.quizzerBodyMediumBold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Tab(selected = selectedTabIndex == 1, onClick = {
                onClick(selectedTabIndex == 1)
                updateQuizTheme(
                    QuizThemeActions.UpdateBackgroundType(ImageColorState.GRADIENT)
                )
            }) {
                Text(
                    stringResource(R.string.gradient),
                    style = QuizzerTypographyDefaults.quizzerBodyMediumBold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Tab(selected = selectedTabIndex == 2, onClick = {
                onClick(selectedTabIndex == 2)
                updateQuizTheme(
                    QuizThemeActions.UpdateBackgroundType(ImageColorState.IMAGE)
                )
            }) {
                Text(
                    stringResource(R.string.image),
                    style = QuizzerTypographyDefaults.quizzerBodyMediumBold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizLayoutSetBackground(){
    QuizzerAndroidTheme {
        QuizLayoutSetBackground(
            backgroundImageColor = ImageColor(
                state = ImageColorState.GRADIENT
            )
        )
    }
}
