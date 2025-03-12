package com.asu1.quizzer.screens.mainScreen

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.asu1.quizcard.HorizontalQuizCardItemLarge
import com.asu1.quizcard.HorizontalQuizCardItemVertical
import com.asu1.quizcardmodel.QuizCardsWithTag
import com.asu1.quizcardmodel.sampleQuizCardsWithTagList
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.utils.LanguageSetter.isKo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

val tabTitles = persistentListOf(
    R.string.main,
    R.string.lck,
    R.string.coding,
    R.string.health,
    R.string.entertainment,
    R.string.history
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    quizCards: List<QuizCardsWithTag>,
    loadQuiz: (String) -> Unit,
    moveToPrivacyPolicy: () -> Unit = {},
    pagerInit: Int = 0,
) {
    val pagerState = rememberPagerState(
        initialPage = pagerInit,
    ){
        tabTitles.size
    }
    val coroutineScope = rememberCoroutineScope()
    var rowWidth by remember { androidx.compose.runtime.mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage + pagerState.currentPageOffsetFraction }
            .collect { pageOffset ->
                coroutineScope.launch {
                    val scrollAmount = (pageOffset * (rowWidth / 5)) // Each item is rowWidth / 5
                    scrollState.scrollTo(scrollAmount.toInt())
                }
            }
    }

    Column(modifier.fillMaxSize()) {
        //TODO: CONTENTS NEEDED FOR EACH TABS
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .onSizeChanged { rowWidth = it.width } // Capture total Row width
//        ) {
//            Row(
//                modifier = Modifier
//                    .horizontalScroll(scrollState),
//                horizontalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                tabTitles.forEachIndexed { index, title ->
//                    RoundTab(
//                        title = stringResource(title),
//                        isSelected = pagerState.currentPage == index,
//                        onClick = {
//                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
//                        }
//                    )
//                }
//            }
//        }
        // Swipeable Pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    quizCards, key = { _, item ->
                        item.tag
                    }
                ) {index, item ->
                    Text(
                        text = remember{if(isKo) changeTagToText(item.tag) else item.tag},
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 2.dp),
                        style = QuizzerTypographyDefaults.quizzerTitleMediumBold,
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
                    Spacer(modifier = Modifier.size(24.dp))
                    PrivacyPolicyRow(moveToPrivacyPolicy)
                }
            }
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
    )
}

@Composable
fun PrivacyPolicyRow(
    moveToPrivacyPolicy: () -> Unit = {},
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {
            moveToPrivacyPolicy()
        }) {
            Text(
                stringResource(R.string.privacy_policy),
                style = QuizzerTypographyDefaults.quizzerLabelMediumMedium,
            )
        }
        Text(
            text = buildString {
                append(stringResource(R.string.contact))
                append(": whwkd122@gmail.com")
            },
            style = QuizzerTypographyDefaults.quizzerLabelMediumMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 16.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:whwkd122@gmail.com?subject=[Quizzer] ".toUri()
                    }
                    context.startActivity(intent)
                },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPolicyRowPreview() {
    PrivacyPolicyRow()
}