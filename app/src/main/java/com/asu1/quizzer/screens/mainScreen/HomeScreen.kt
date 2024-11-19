package com.asu1.quizzer.screens.mainScreen

import HorizontalQuizCardItemLarge
import HorizontalQuizCardItemVertical
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.getSampleQuizCard
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.QuizCardMainViewModel.QuizCardsWithTag
import loadImageAsByteArray

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    quizCards: List<QuizCardsWithTag>,
    loadQuiz: (String) -> Unit,
    navController: NavController,
) {

    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            quizCards.size,
            key = { quizCards ->
                quizCards.hashCode()
            }
        ) { index ->
            Text(
                text = quizCards[index].tag,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
            if (index == 0) {
                HorizontalQuizCardItemLarge(quizCards = quizCards[index].quizCards,
                    onClick = { uuid -> loadQuiz(uuid) })
            } else {
                HorizontalQuizCardItemVertical(quizCards = quizCards[index].quizCards,
                    onClick = { uuid -> loadQuiz(uuid) })
            }
        }
        item {
            Spacer(modifier = Modifier.size(8.dp))
            PrivacyPolicyRow(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    val quizCard = getSampleQuizCard()

    val quizCardsWithTag = QuizCardsWithTag(
        tag = "tag1",
        quizCards = listOf(quizCard, quizCard, quizCard, quizCard, quizCard)
    )
    HomeScreen(
        quizCards = listOf(quizCardsWithTag, quizCardsWithTag, quizCardsWithTag),
        loadQuiz = {},
        navController = rememberNavController()
    )
}

@Composable
fun PrivacyPolicyRow(navController: NavController) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {
            NavMultiClickPreventer.navigate(navController, Route.PrivacyPolicy)
        }) {
            Text(stringResource(R.string.privacy_policy))
        }
        Text(
            text = stringResource(R.string.contact) + ": whwkd122@gmail.com",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPolicyRowPreview() {
    val navController = rememberNavController()
    PrivacyPolicyRow(navController)
}