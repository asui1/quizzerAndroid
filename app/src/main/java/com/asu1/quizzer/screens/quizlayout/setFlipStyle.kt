package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.screens.getQuizLayoutState
import com.asu1.quizzer.states.QuizLayoutState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.loadImageFromDrawable

@Composable
fun QuizLayoutSetFlipStyle(quizLayoutState: QuizLayoutState, proceed: () -> Unit) {
    val context = LocalContext.current
    val layoutImages = remember {
        listOf(
            R.drawable.layoutoption1,
            R.drawable.layoutoption2,
            R.drawable.layoutoption3,
            R.drawable.layoutoption4,
        )
    }
    var selectedFlipStyle by remember { mutableStateOf(quizLayoutState.flipStyle.value) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Set Flip Style",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.size(16.dp))
        ImageRow(layoutImages[0], layoutImages[1], selectedFlipStyle, 0) { selectedFlipStyle = it }
        ImageRow(layoutImages[2], layoutImages[3], selectedFlipStyle, 2) { selectedFlipStyle = it }
    }

    LaunchedEffect(selectedFlipStyle) {
        if(selectedFlipStyle != null){
            quizLayoutState.setFlipStyle(selectedFlipStyle!!)
        }
    }
}

@Composable
fun ImageRow(
    image1: Int,
    image2: Int,
    selectedFlipStyle: Int?,
    givenIndex: Int,
    onImageSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ImageWithBorder(image1, givenIndex, selectedFlipStyle, onImageSelected)
        ImageWithBorder(image2, givenIndex+1, selectedFlipStyle, onImageSelected)
    }
}

@Composable
fun ImageWithBorder(
    image: Int,
    flipStyle: Int,
    selectedFlipStyle: Int?,
    onImageSelected: (Int) -> Unit
) {
    val borderColor = if (selectedFlipStyle == flipStyle) MaterialTheme.colorScheme.primary else Color.Transparent
    Image(
        painter = painterResource(id = image), // Replace with your app icon resource
        contentDescription = "Flip Style $flipStyle",
        modifier = Modifier
            .padding(4.dp)
            .border(BorderStroke(2.dp, borderColor), shape = RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures {
                    onImageSelected(flipStyle)
                }
            },
    )
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutSetFlipStylePreview() {
    QuizzerAndroidTheme {
        QuizLayoutSetFlipStyle(
            quizLayoutState = getQuizLayoutState(),
            proceed = {},
        )
    }
}
