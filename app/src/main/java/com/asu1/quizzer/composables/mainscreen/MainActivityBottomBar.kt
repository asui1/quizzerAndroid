package com.asu1.quizzer.composables.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainActivityBottomBar(
    bottomBarSelection: Int = 0,
    setSelectedTab: (Int) -> Unit = {},
    navigateToCreateQuizLayout: () -> Unit = {},
    ) {
    val defaultIconSize = 24.dp

    BottomAppBar(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = {
                        setSelectedTab(0)
                    },
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
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = "Trends", modifier = Modifier.size(defaultIconSize))
                }
                FloatingActionButton(
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                    onClick = { navigateToCreateQuizLayout() },
                    shape = CircleShape,
                    modifier = Modifier
                        .testTag("MainScreenCreateQuiz"),
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Create Quiz",
                        modifier = Modifier.size(defaultIconSize * 1.5f)
                    )
                }
                IconButton(
                    onClick = { setSelectedTab(2) },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.Default.BarChart, contentDescription = "User Statistics", modifier = Modifier.size(defaultIconSize))
                }
                IconButton(
                    onClick = { setSelectedTab(3) },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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
    com.asu1.resources.QuizzerAndroidTheme {
        MainActivityBottomBar(
        )
    }
}