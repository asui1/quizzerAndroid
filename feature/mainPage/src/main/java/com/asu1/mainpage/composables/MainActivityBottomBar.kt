package com.asu1.mainpage.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.button.IconWithTextBody
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun MainActivityBottomBar(
    bottomBarSelection: Int = 0,
    setSelectedTab: (Int) -> Unit = {},
    navigateToCreateQuizLayout: () -> Unit = {},
) {
    val defaultIconSize = 24.dp


    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconWithTextBody(
            text = stringResource(R.string.home),
            onClick = { setSelectedTab(0) }
        ) {
            Icon(
                imageVector = if (bottomBarSelection == 0) Icons.Filled.Home else Icons.Outlined.Home,
                tint = if (bottomBarSelection == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                contentDescription = "Home",
                modifier = Modifier.size(defaultIconSize)
            )
        }
        IconWithTextBody(
            text = stringResource(R.string.trends),
            onClick = { setSelectedTab(1) }
        ) {
            Icon(
                imageVector = if (bottomBarSelection == 1) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Outlined.TrendingUp,
                tint = if (bottomBarSelection == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                contentDescription = "Trends",
                modifier = Modifier.size(defaultIconSize)
            )
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
                modifier = Modifier.size(defaultIconSize)
            )
        }
        IconWithTextBody(
            text = stringResource(R.string.ranks),
            onClick = { setSelectedTab(2) }
        ) {
            Icon(
                imageVector = if (bottomBarSelection == 2) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                tint = if (bottomBarSelection == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                contentDescription = "User Statistics",
                modifier = Modifier.size(defaultIconSize)
            )
        }
        IconWithTextBody(
            text = stringResource(R.string.setting),
            onClick = { setSelectedTab(3) }
        ) {
            Icon(
                imageVector = if (bottomBarSelection == 3) Icons.Filled.Settings else Icons.Outlined.Settings,
                tint = if (bottomBarSelection == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                contentDescription = "Settings",
                modifier = Modifier.size(defaultIconSize)
            )
        }
    }
}

@Preview
@Composable
fun MainActivityBottomBarPreview(){
    QuizzerAndroidTheme {
        MainActivityBottomBar(
        )
    }
}