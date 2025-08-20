package com.asu1.mainpage.screens.mainScreen

import androidx.annotation.StringRes
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.button.IconWithTextBody
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@Composable
fun MainActivityBottomBar(
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit,
    onCreateQuiz: () -> Unit
) {
    val items = listOf(
        NavBarItem(R.string.home, Icons.Filled.Home,
            Icons.Outlined.Home, 0),
        NavBarItem(R.string.trends, Icons.AutoMirrored.Filled.TrendingUp,
            Icons.AutoMirrored.Outlined.TrendingUp, 1),
        null, // placeholder for FAB
        NavBarItem(R.string.ranks, Icons.Filled.BarChart,
            Icons.Outlined.BarChart, 2),
        NavBarItem(R.string.setting, Icons.Filled.Settings,
            Icons.Outlined.Settings, 3)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            when {
                item == null -> CreateQuizButton(onClick = onCreateQuiz)
                else -> NavBarIcon(
                    item = item,
                    isSelected = item.fixedIndex == selectedTab,
                    onClick = { onTabSelected(item.fixedIndex) }
                )
            }
        }
    }
}

private data class NavBarItem(
    @param:StringRes val labelRes: Int,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val fixedIndex: Int,
)

@Composable
private fun NavBarIcon(
    item: NavBarItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    IconWithTextBody(
        text = stringResource(item.labelRes),
        onClick = onClick
    ) {
        Icon(
            imageVector = if (isSelected) item.activeIcon else item.inactiveIcon,
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface,
            contentDescription = stringResource(item.labelRes),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun CreateQuizButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        elevation = FloatingActionButtonDefaults.elevation(
            0.dp, 0.dp,
            0.dp, 0.dp),
        shape = CircleShape,
        modifier = Modifier.testTag("MainScreenCreateQuiz")
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_new_quiz),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
fun MainActivityBottomBarPreview(){
    QuizzerAndroidTheme {
        MainActivityBottomBar(
            selectedTab = 0,
            onTabSelected = {},
            onCreateQuiz = {},
        )
    }
}
