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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
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
) {
    val selectedSaver = remember {
        mapSaver(
            save = { mapOf("ord" to it.ordinal) },
            restore = { EffectTypes.entries[it["ord"] as Int] }
        )
    }
    var selectedType by rememberSaveable(stateSaver = selectedSaver) {
        mutableStateOf(EffectTypes.NONE)
    }
    val effects by remember(selectedType) { mutableStateOf(Effect.fromType(selectedType)) }

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
        ) {
            Text(
                text = stringResource(R.string.pick_effect),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            EffectTypeTabs(
                selected = selectedType,
                onSelect = { selectedType = it }
            )

            EffectGrid(
                effects = effects,
                currentSelection = currentSelection,
                onPick = {
                    onClick(it)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun EffectTypeTabs(
    selected: EffectTypes,
    onSelect: (EffectTypes) -> Unit,
    modifier: Modifier = Modifier,
) {
    SecondaryScrollableTabRow(
        selectedTabIndex = selected.ordinal,
        edgePadding = 0.dp,
        modifier = modifier
    ) {
        EffectTypes.entries.forEach { type ->
            Tab(
                selected = selected == type,
                onClick = { onSelect(type) },
            ) {
                Text(
                    text = stringResource(type.stringResource),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun EffectGrid(
    effects: List<Effect>,
    currentSelection: Effect,
    onPick: (Effect) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(
            items = effects,
            key = { it.ordinal } // <- ensure Effect exposes a stable id; else use it.hashCode()
        ) { effect ->
            EffectGridItem(
                effect = effect,
                selected = effect == currentSelection,
                onClick = { onPick(effect) }
            )
        }
    }
}

@Composable
private fun EffectGridItem(
    effect: Effect,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.small
            )
            .clickable(onClick = onClick)
            .padding(8.dp)
            .testTag("DesignScoreCardAnimationButton_${effect.ordinal}")
    ) {
        // Replace with Icon(painterResource(effect.iconRes), ...) if iconRes is a drawable id
        Text(
            text = effect.iconRes,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
        )
        Text(
            text = stringResource(effect.stringId),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
        )
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