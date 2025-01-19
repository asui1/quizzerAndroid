package com.asu1.quizzer.screens.mainScreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.musics.MusicPlayer
import com.asu1.quizcard.HorizontalQuizCardItemLarge
import com.asu1.quizcard.HorizontalQuizCardItemVertical
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.constants.sampleQuizCardsWithTagList
import com.asu1.quizzer.viewModels.QuizCardMainViewModel.QuizCardsWithTag

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    quizCards: List<QuizCardsWithTag>,
    loadQuiz: (String) -> Unit,
    isKo: Boolean = false,
    navController: NavController,
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(
            quizCards, key = { index, item ->
                item.tag
            }
        ) {index, item ->
            Text(
                text = remember{if(isKo) changeTagToText(item.tag) else item.tag},
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            if (index == 0) {
                HorizontalQuizCardItemLarge(quizCards = item.quizCards,
                    onClick = { uuid -> loadQuiz(uuid) })
            } else {
                HorizontalQuizCardItemVertical(quizCards = item.quizCards,
                    onClick = { uuid -> loadQuiz(uuid) })
            }
        }
        item {
            MusicPlayer()
            Spacer(modifier = Modifier.size(24.dp))
            PrivacyPolicyRow(navController)
        }
    }
}

fun changeTagToText(tag: String): String{
    val newTag = when {
        tag.startsWith("Most Viewed") -> "인기순"
        tag.startsWith("Most Recent") -> "신규 퀴즈"
        tag.startsWith("With Tag : ") -> buildString {
            append("이런 주제는 어떠세요? \"")
            append(tag.removePrefix("With Tag : "))
            append("\"")
        }
        else -> tag
    }
    return newTag
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    val quizCardsWithTagList = sampleQuizCardsWithTagList
    HomeScreen(
        quizCards = quizCardsWithTagList,
        loadQuiz = {},
        navController = rememberNavController()
    )
}

@Composable
fun PrivacyPolicyRow(navController: NavController) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {
            navController.navigate(Route.PrivacyPolicy){
                launchSingleTop = true
            }
        }) {
            Text(stringResource(R.string.privacy_policy))
        }
        Text(
            text = buildString {
                append(stringResource(R.string.contact))
                append(": whwkd122@gmail.com")
            },
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 16.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:whwkd122@gmail.com?subject=[Quizzer] ")
                    }
                    context.startActivity(intent)
                },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPolicyRowPreview() {
    val navController = rememberNavController()
    PrivacyPolicyRow(navController)
}