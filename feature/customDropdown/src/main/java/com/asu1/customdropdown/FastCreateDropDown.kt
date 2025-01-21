package com.asu1.customdropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.custombuttons.IconButtonWithText
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.ShaderType

@Composable
fun FastCreateDropDown(
    showDropdownMenu: Boolean,
    labelText: String = "",
    onClick: (Int) -> Unit = {},
    onChangeDropDown: (Boolean) -> Unit = {},
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
            onClick = {
                onChangeDropDown(true)
            },
            modifier = Modifier.testTag(testTag),
            description = "Open Dropdown Menu",
            iconSize = iconSize,
        )
        DropdownMenu(
            expanded = showDropdownMenu,
            onDismissRequest = { onChangeDropDown(false) },
        ) {
            inputItems.withIndex().forEach { (index, item) ->
                DropdownMenuItem( //DesignScoreCardShaderButton
                    modifier = Modifier.testTag("$testTag${index}")
                        .background(
                            color = if(index == currentSelection) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                        ),
                    text = {
                        Text(
                            text = stringResource(item),
                        )
                    },
                    onClick = {
                        onClick(index)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FastCreateDropDownPreview() {
    QuizzerAndroidTheme {
        FastCreateDropDown(
            showDropdownMenu = false,
            labelText = "Select",
            onClick = { },
            onChangeDropDown = { },
            inputItems = ShaderType.values().map { it.shaderName },
            imageVector = Icons.Default.Animation,
            testTag = "DesignScoreCardDropDown",
            modifier = Modifier.size(50.dp)
        )
    }
}