package com.asu1.quizzer.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.quizzer.composables.UserRankComposableList
import com.asu1.quizzer.model.UserRank

@Composable
fun UserRankScreen(
    userRanks: List<UserRank>,
    modifier: Modifier = Modifier,
) {
    UserRankComposableList(
        userRanks = userRanks,
        modifier = modifier,
    )
}



@Preview(showBackground = true)
@Composable
fun UserRankScreenPreview() {
    val userRank = UserRank(
        "Nickname",
        "https://lh3.googleusercontent.com/a/ACg8ocJfoHUjigfS1fBoyEPXLv1pusBvf7WTJAfUoQV8YhPjr4Whq98=s96-c",
        342.3f,
        5,
    )
    val userRanks = mutableListOf<UserRank>()
    for (i in 1..20) {
        userRanks.add(
            userRank
        )
    }
    UserRankScreen(
        userRanks = userRanks
    )
}