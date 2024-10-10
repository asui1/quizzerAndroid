package com.asu1.quizzer.screens.quizlayout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.viewModels.FlipStyle

@Composable
fun QuizLayoutSetFlipStyle(
    flipStyle: FlipStyle = FlipStyle.Center,
    onFlipStyleUpdate: (FlipStyle) -> Unit = {},
    proceed: () -> Unit = {},
) {
    val layoutImages = remember {
        listOf(
            R.drawable.layoutoption1,
            R.drawable.layoutoption2,
            R.drawable.layoutoption3,
            R.drawable.layoutoption4,
        )
    }
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
        ImageRow(layoutImages[0], layoutImages[1], flipStyle, 0) { onFlipStyleUpdate(it) }
        ImageRow(layoutImages[2], layoutImages[3], flipStyle, 2) { onFlipStyleUpdate(it) }
    }
}

@Composable
fun ImageRow(
    image1: Int,
    image2: Int,
    selectedFlipStyle: FlipStyle,
    givenIndex: Int,
    onImageSelected: (FlipStyle) -> Unit
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
    selectedFlipStyle: FlipStyle,
    onImageSelected: (FlipStyle) -> Unit
) {
    val borderColor = if (selectedFlipStyle.value == flipStyle) MaterialTheme.colorScheme.primary else Color.Transparent
    Image(
        painter = painterResource(id = image), // Replace with your app icon resource
        contentDescription = "Flip Style $flipStyle",
        modifier = Modifier
            .padding(4.dp)
            .border(BorderStroke(2.dp, borderColor), shape = RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures {
                    onImageSelected(FlipStyle.from(flipStyle))
                }
            },
    )
}

@Preview(showBackground = true)
@Composable
fun QuizLayoutSetFlipStylePreview() {
    QuizzerAndroidTheme {
        QuizLayoutSetFlipStyle(
        )
    }
}
