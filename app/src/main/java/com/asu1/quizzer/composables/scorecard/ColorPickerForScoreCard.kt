package com.asu1.quizzer.composables.scorecard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.composables.base.ColorPicker

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
        text = "Select Text Color",
    )
}