package com.asu1.mainpage.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.customComposable.topBar.QuizzerTopBarBase
import com.asu1.activityNavigation.Route
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R

@Composable
fun PrivacyPolicy(navController: NavController) {
    Scaffold(
        topBar = {
            QuizzerTopBarBase(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer),
                header = {
                    IconButton(onClick = { navController.popBackStack(
                        Route.Home,
                        inclusive = false
                    ) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Move Back to Home")
                    }
                },
                body = {
                    Text(
                        text = stringResource(R.string.privacy_policy),
                        style = QuizzerTypographyDefaults.quizzerBodySmallNormal,
                    )
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.privacy_policy_body)
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewPrivacyPolicy() {
    val navController = rememberNavController()
    com.asu1.resources.QuizzerAndroidTheme {
        PrivacyPolicy(
            navController = navController
        )
    }
}