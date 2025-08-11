package com.asu1.quiz.scorecard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.colorPicker.ColorPicker

@Composable
fun TextColorPickerModalSheet(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    text: String = "",
    colorName: String = "",
    toggleBlendMode: @Composable () -> Unit = {},
    resetToTransparent: @Composable () -> Unit = {},
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
            .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
            .padding(24.dp)
    ){
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        toggleBlendMode()
        resetToTransparent()
        ColorPicker(
            modifier = Modifier.height(300.dp),
            colorName = colorName,
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
        toggleBlendMode = {},
        resetToTransparent = {},
    )
}
