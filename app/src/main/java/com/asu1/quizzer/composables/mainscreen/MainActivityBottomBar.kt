package com.asu1.quizzer.composables.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme

@Composable
fun MainActivityBottomBar(onDrawerOpen: () -> Unit = {}, bottomBarSelection: Int = 0,
                          navigateToQuizLayoutBuilder: () -> Unit = {},
                          setSelectedTab: (Int) -> Unit = {}) {
    val defaultIconSize = 24.dp

    BottomAppBar(
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = {
                        setSelectedTab(0)
                    },
                    modifier = Modifier.weight(1f),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(defaultIconSize))
                }
                IconButton(
                    onClick = {
                        setSelectedTab(1)
                    },
                    modifier = Modifier.weight(1f),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = "Trends", modifier = Modifier.size(defaultIconSize))
                }
                IconButton(
                    onClick = { navigateToQuizLayoutBuilder() },
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("MainScreenCreateQuiz"),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.Default.AddCircleOutline, contentDescription = "Create Quiz", modifier = Modifier.size(1.5f * defaultIconSize))
                }
                IconButton(
                    onClick = { setSelectedTab(2) },
                    modifier = Modifier.weight(1f),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.Default.BarChart, contentDescription = "User Statistics", modifier = Modifier.size(defaultIconSize))
                }
                IconButton(
                    onClick = {
                        onDrawerOpen()
                    },
                    modifier = Modifier.weight(1f),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", modifier = Modifier.size(defaultIconSize))
                }
            }
        }
    )
}

@Preview
@Composable
fun MainActivityBottomBarPreview(){
    QuizzerAndroidTheme {
        MainActivityBottomBar(
        )
    }
}