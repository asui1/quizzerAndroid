package com.asu1.mainpage.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.asu1.mainpage.composables.UserRankComposableList
import com.asu1.mainpage.viewModels.QuizCardViewModel
import com.asu1.userdatamodels.UserRank

@Composable
fun RanksPage(
    ranks: List<UserRank>,
) {
    val vm: QuizCardViewModel = hiltViewModel()

    UserRankComposableList(
        userRanks = ranks,
        getMoreUserRanks = { vm.getMoreUserRanks() },
        modifier = Modifier.fillMaxSize()
    )
}
