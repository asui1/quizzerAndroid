package com.asu1.customComposable.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.utils.shaders.ShaderType

@Composable
fun FastCreateDropDownWithTextButton(
    modifier: Modifier = Modifier,
    showDropdownMenu: Boolean,
    labelText: String = "",
    onClick: (Int) -> Unit = {},
    onChangeDropDown: (Boolean) -> Unit = {},
    inputStringResourceItems: List<Int> = emptyList(),
    testTag: String = "",
    currentSelection: Int = -1,
) {
    var boxWidth by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onGloballyPositioned() { coordinates ->
                boxWidth = coordinates.size.width
            }
            .padding(4.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp)),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .testTag(testTag)
                .clickable{ onChangeDropDown(!showDropdownMenu) }
                .defaultMinSize(minHeight = 48.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = labelText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Open Background gradient selector"
            )
        }

        DropdownMenu(
            expanded = showDropdownMenu,
            onDismissRequest = { onChangeDropDown(false) },
        ) {
            inputStringResourceItems.withIndex().forEach { (index, item) ->
                DropdownMenuItem(
                    modifier = Modifier
                        .then(
                            if (boxWidth > 0) {
                                Modifier.width(with(density) { boxWidth.toDp() })
                            } else Modifier
                        )
                        .testTag("$testTag${index+1}")
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
fun PreviewFastCreateDropDownWithTextButton() {
    QuizzerAndroidTheme {
        FastCreateDropDownWithTextButton(
            showDropdownMenu = true,
            labelText = "Select",
            onClick = { },
            onChangeDropDown = { },
            inputStringResourceItems = ShaderType.entries.map { it.shaderName },
            testTag = "DesignScoreCardDropDown",
        )
    }
}