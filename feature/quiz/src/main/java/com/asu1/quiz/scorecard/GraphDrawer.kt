package com.asu1.quiz.scorecard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerAndroidTheme
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.GridProperties
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun GraphDrawer(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    distribution: PersistentList<Int>,
    textColor: Color,
    selectedIndex: Int,
    selectedColor: Color,
){
    val brush = remember(textColor) {
        Brush.linearGradient(listOf(textColor, textColor))
    }
    val selectedBrush = remember(selectedColor){
        Brush.linearGradient(listOf(selectedColor, selectedColor))
    }
    val maxValue = remember(distribution){
        (((distribution.max() + 3) / 4) * 4).toDouble()
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        header()
        ColumnChart(
            data = remember(distribution){
                distribution.mapIndexed { index, item ->
                    Bars(
                        label = index.toString(),
                        values = listOf(
                            Bars.Data(
                                value = item.toDouble(),
                                color = if(index == selectedIndex) selectedBrush else brush
                            )
                        ),
                    )
                }
            },
            barProperties = BarProperties(
                spacing = 1.dp,
                thickness = 10.dp,
            ),
            maxValue = maxValue,
            gridProperties = GridProperties(
                enabled = false,
            ),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GraphDrawerPreview(){
    QuizzerAndroidTheme {
        GraphDrawer(
            header = {Text("Graph Example")},
            distribution = persistentListOf(1, 2, 3, 4, 5, 6),
            textColor = Color.Red,
            selectedIndex = 3,
            selectedColor = Color.Blue
        )
    }
}