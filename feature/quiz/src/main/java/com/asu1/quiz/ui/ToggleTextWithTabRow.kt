package com.asu1.quiz.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ToggleTextWithTabRow(
    modifier: Modifier = Modifier,
    tabTexts: PersistentList<String>,
    selectedItem: Int,
    onClick: (Boolean) -> Unit = {},
) {
    SecondaryTabRow(
        selectedTabIndex = selectedItem,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        tabTexts.forEachIndexed { index, title ->
            Tab(
                selected = selectedItem == index,
                onClick = { onClick(index == 1) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToggleTextWithSwitchPreview() {
    ToggleTextWithTabRow(
        tabTexts = persistentListOf("COLOR", "HUE"),
        selectedItem = 0,
    )
}
