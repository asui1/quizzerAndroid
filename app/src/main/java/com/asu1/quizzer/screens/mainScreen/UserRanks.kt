package com.asu1.quizzer.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.quizzer.composables.UserRankComposableList
import com.asu1.quizzer.model.UserRank
import com.asu1.quizzer.model.userRankSample

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
    val userRank = userRankSample
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