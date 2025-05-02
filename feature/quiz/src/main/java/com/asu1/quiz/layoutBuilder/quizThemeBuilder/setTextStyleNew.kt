package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.flipper.HorizontalFlipper
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.BodyTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import com.asu1.quiz.viewmodel.quizLayout.QuizThemeActions
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
import com.asu1.resources.borders
import com.asu1.resources.colors
import com.asu1.resources.fonts
import com.asu1.resources.outlines

@Composable
fun QuizLayoutSetTextStyle(
    modifier: Modifier = Modifier,
    questionStyle: List<Int>,
    bodyStyle: List<Int>,
    answerStyle: List<Int>,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
    scrollTo: () -> Unit = {},
){
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var isOpen by remember { mutableStateOf(false) }
    val textStyle: List<Int> = when (selectedTabIndex) {
        0 -> questionStyle
        1 -> bodyStyle
        2 -> answerStyle
        else -> emptyList()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.text_style_colon),
            fontWeight = FontWeight.ExtraBold,
        )
        SetTextStyleTabRow(
            selectedTabIndex = selectedTabIndex,
            onClick = { index, clickedSame ->
                isOpen = if(clickedSame){
                    !isOpen
                }else{
                    selectedTabIndex = index
                    true
                }
                if(isOpen) scrollTo()
                if(!isOpen && clickedSame) selectedTabIndex = -1
            }
        )

        AnimatedVisibility(
            visible = isOpen,
        ) {
            HorizontalDivider()
            TextStyleFlipper(
                selectedTabIndex = selectedTabIndex,
                textStyle = textStyle,
                updateQuizTheme = updateQuizTheme,
            )
        }
    }
}

@Composable
fun SetTextStyleTabRow(
    selectedTabIndex: Int,
    onClick: (Int, Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .height(55.dp)
            .fillMaxWidth()
    ) {
        StyleTabItem(
            modifier = Modifier
                .testTag("setTextStyleQuestionStyleTab"),
            index = 0,
            selectedTabIndex = selectedTabIndex,
            textComposable = { modifier ->
                QuestionTextStyle.GetTextComposable(stringResource(R.string.question), modifier)
            },
            onClick = onClick
        )

        StyleTabItem(
            modifier = Modifier
                .testTag("setTextStyleBodyStyleTab"),
            index = 1,
            selectedTabIndex = selectedTabIndex,
            textComposable = { modifier ->
                BodyTextStyle.GetTextComposable(stringResource(R.string.body), modifier)
            },
            onClick = onClick
        )

        StyleTabItem(
            modifier = Modifier
                .testTag("setTextStyleAnswerStyleTab"),
            index = 2,
            selectedTabIndex = selectedTabIndex,
            textComposable = { modifier ->
                AnswerTextStyle.GetTextComposable(stringResource(R.string.answer), modifier)
            },
            onClick = onClick
        )
    }
}

@Composable
private fun StyleTabItem(
    modifier: Modifier = Modifier,
    index: Int,
    selectedTabIndex: Int,
    textComposable: @Composable (Modifier) -> Unit,
    onClick: (Int, Boolean) -> Unit,
) {
    var textWidth by remember { mutableIntStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onClick(index, selectedTabIndex == index) }
    ) {
        textComposable(
            Modifier.onGloballyPositioned { coordinates ->
                textWidth = coordinates.size.width
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(2.dp)
                .width(with(LocalDensity.current) { textWidth.toDp() })
                .background(
                    if (selectedTabIndex == index) MaterialTheme.colorScheme.primary
                    else Color.Transparent
                )
        )
    }
}

val flipperConfigs = listOf(
    Triple("SetTextStyleFont", fonts, 0),
    Triple("SetTextStyleColor", colors, 1),
    Triple("SetTextStyleBorder", borders, 2),
    Triple("SetTextStyleOutline", outlines, 4),
)

@Composable
fun TextStyleFlipper(
    selectedTabIndex: Int,
    textStyle: List<Int>,
    updateQuizTheme: (QuizThemeActions) -> Unit = {},
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        flipperConfigs.forEach { (key, items, index) ->
            HorizontalFlipper(
                items = items,
                currentIndex = textStyle.getOrNull(index) ?: 0,
                onNext = {
                    updateQuizTheme(
                        QuizThemeActions.UpdateTextStyle(
                            selectedTabIndex, index, true
                        )
                    )
                },
                onPrevious = {
                    updateQuizTheme(
                        QuizThemeActions.UpdateTextStyle(
                            selectedTabIndex, index, false
                        )
                    )
                },
                key = key
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizLayoutSetTextStyle(){
    QuizzerAndroidTheme {
        QuizLayoutSetTextStyle(
            questionStyle = listOf(0, 0, 0, 0, 0),
            bodyStyle = listOf(0, 0, 0, 0, 0),
            answerStyle = listOf(0, 0, 0, 0, 0),
        )
    }
}