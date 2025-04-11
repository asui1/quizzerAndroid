package com.asu1.quiz.scorecard.scorecardToolsDialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.imagecolor.Effect
import com.asu1.imagecolor.EffectTypes
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun EffectPickerDialog(
    onClick: (Effect) -> Unit = {},
    currentSelection: Effect = Effect.NONE,
    onDismiss: () -> Unit,
){
    var selectedEffectType by remember { mutableStateOf(EffectTypes.NONE) }

    val filteredEffects by remember(selectedEffectType) {
        derivedStateOf {
            Effect.fromType(selectedEffectType)
        }
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                stringResource(R.string.pick_effect),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            SecondaryScrollableTabRow(
                selectedTabIndex = selectedEffectType.ordinal,
                edgePadding = 0.dp,
            ) {
                EffectTypes.entries.forEach { item ->
                    Tab(
                        selected = selectedEffectType == item,
                        onClick = { selectedEffectType = item },
                    ) {
                        Text(
                            text = stringResource(item.stringResource),
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(filteredEffects, key = { index, effect -> effect }) { index, item ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(
                                color = if (item == currentSelection)
                                    MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceContainer,
                                shape = MaterialTheme.shapes.small
                            )
                            .clickable {
                                onClick(item)
                                onDismiss()
                            }
                            .padding(8.dp)
                            .testTag("DesignScoreCardAnimationButton$index"),
                    ) {
                        Text(
                            text = item.iconRes,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                        )
                        Text(
                            text = stringResource(item.stringId),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEffectPickerDialog(){
    QuizzerAndroidTheme {
        EffectPickerDialog(
            onClick = {},
            onDismiss = {},
        )
    }
}