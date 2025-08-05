package com.asu1.customComposable.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.utils.shaders.ShaderType

@Composable
fun FastCreateDropDownWithTextButton(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    labelText: String = "",
    onToggleExpanded: (Boolean) -> Unit = {},
    onItemSelected: (Int) -> Unit = {},
    itemResIds: List<Int> = emptyList(),
    testTag: String = "",
    selectedIndex: Int = -1,
) {
    // BoxWithConstraints gives us maxWidth in Dp, so no need for boxWidth+density
    BoxWithConstraints(
        modifier = modifier
            .padding(4.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
    ) {
        val menuWidth = maxWidth

        // 1) the “button” row
        DropDownButtonRow(
            labelText = labelText,
            expanded = expanded,
            onToggle = { onToggleExpanded(!expanded) },
            testTag = testTag
        )

        // 2) the actual dropdown
        DropDownMenuContent(
            expanded = expanded,
            width = menuWidth,
            itemResIds = itemResIds,
            selectedIndex = selectedIndex,
            testTag = testTag,
            onItemClick = { index ->
                onItemSelected(index)
                onToggleExpanded(false)
            }
        )
    }
}

@Composable
private fun DropDownButtonRow(
    labelText: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .testTag(testTag)
            .clickable(onClick = onToggle),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = labelText, style = MaterialTheme.typography.bodyMedium)
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = if (expanded) "Collapse" else "Expand"
        )
    }
}

@Composable
private fun DropDownMenuContent(
    expanded: Boolean,
    width: Dp,
    itemResIds: List<Int>,
    selectedIndex: Int,
    testTag: String,
    onItemClick: (Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onItemClick(selectedIndex) },
        modifier = Modifier.width(width)
    ) {
        itemResIds.forEachIndexed { index, resId ->
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(resId))
                },
                onClick = { onItemClick(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("$testTag${index + 1}")
                    .background(
                        if (index == selectedIndex)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceContainer
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFastCreateDropDownWithTextButton() {
    QuizzerAndroidTheme {
        FastCreateDropDownWithTextButton(
            expanded = true,
            labelText = "Select",
            onItemSelected = { },
            onToggleExpanded = { },
            itemResIds = ShaderType.entries.map { it.shaderName },
            testTag = "DesignScoreCardDropDown",
        )
    }
}
