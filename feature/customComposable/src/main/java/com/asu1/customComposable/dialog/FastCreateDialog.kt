package com.asu1.customComposable.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.asu1.customComposable.button.IconButtonWithText

@Composable
fun FastCreateDialog(
    showDialog: Boolean,
    labelText: String = "",
    onClick: (Int) -> Unit = {},
    onChangeDialog: (Boolean) -> Unit = {},
    inputItems: List<Int> = emptyList(),
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    testTag: String = "",
    iconSize: Dp = 32.dp,
    currentSelection: Int = -1,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        IconButtonWithText(
            imageVector = imageVector,
            text = labelText,
            onClick = { onChangeDialog(true) },
            modifier = Modifier.testTag(testTag),
            description = "Open Dialog",
            iconSize = iconSize,
        )
        if (showDialog) {
            Dialog(onDismissRequest = { onChangeDialog(false) }) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Using LazyVerticalGrid to create a grid layout with 3 columns
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        itemsIndexed(inputItems) { index, item ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (index == currentSelection)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else MaterialTheme.colorScheme.surfaceContainer,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .clickable {
                                        onClick(index)
                                        onChangeDialog(false)
                                    }
                                    .padding(8.dp)
                                    .testTag("$testTag$index"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(item),
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
