package com.asu1.mainpage.screens.mainScreen

import SnackBarManager
import ToastType
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.activityNavigation.Route
import com.asu1.mainpage.screens.HomePage
import com.asu1.mainpage.screens.RanksPage
import com.asu1.mainpage.screens.SettingsPage
import com.asu1.mainpage.screens.TrendsPage
import com.asu1.mainpage.viewModels.QuizCardViewModel
import com.asu1.quiz.viewmodel.UserViewModel
import com.asu1.quiz.viewmodel.sampleUserData
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.QuizCardsWithTag
import com.asu1.quizcardmodel.sampleQuizCardList
import com.asu1.quizcardmodel.sampleQuizCardsWithTagList
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
import com.asu1.userdatamodels.UserRank
import com.asu1.userdatamodels.sampleUserRankList
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    navigateTo: (Route) -> Unit,
    loadQuiz: (String) -> Unit,
    moveHome: () -> Unit
) {
    val quizCardViewModel: QuizCardViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()

    // 1) collect all state and effects into one state holder
    val state = rememberMainScreenState(
        quizCardViewModel = quizCardViewModel,
        userViewModel = userViewModel,
    )

    // 2) render UI based on state
    MainScreenUI(
        state           = state,
        navController   = navController,
        navigateTo      = navigateTo,
        loadQuiz        = loadQuiz,
        moveHome        = moveHome
    )
}

// --- State holder encapsulates all mutable state & side effects ---
@Immutable  // data classes are automatically considered immutable
data class MainScreenState(
    val pagerState: PagerState,
    val quizState:  QuizState,
    val userState:  UserState
)

@Immutable
data class QuizState(
    val cards:   List<QuizCardsWithTag>,
    val trends:  List<QuizCard>,
    val ranks:   List<UserRank>
)

@Immutable
data class UserState(
    val data:       UserViewModel.UserData?,
    val isLoggedIn: Boolean
)

@Composable
private fun rememberMainScreenState(
    quizCardViewModel: QuizCardViewModel,
    userViewModel:         UserViewModel,
): MainScreenState {
    // 1) your pager
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })

    LaunchedEffect(pagerState.currentPage) {
        quizCardViewModel.tryUpdate(pagerState.currentPage)
    }

    // 2) collect all quiz‐related bits
    val quizCards    by quizCardViewModel.quizCards.collectAsStateWithLifecycle()
    val quizTrends   by quizCardViewModel.visibleQuizTrends.collectAsStateWithLifecycle()
    val userRanks    by quizCardViewModel.visibleUserRanks.collectAsStateWithLifecycle()

    // 3) collect all user‐related bits
    val userData   by userViewModel.userData.observeAsState()
    val isLoggedIn by userViewModel.isUserLoggedIn.observeAsState(false)

    // 6) build your sub‐state objects
    val quizState = remember(quizCards, quizTrends, userRanks) {
        QuizState(
            cards  = quizCards,
            trends = quizTrends,
            ranks  = userRanks
        )
    }
    val userState = remember(userData, isLoggedIn) {
        UserState(
            data       = userData,
            isLoggedIn = isLoggedIn
        )
    }

    // 7) return the new MainScreenState
    return remember(pagerState, quizState, userState) {
        MainScreenState(
            pagerState = pagerState,
            quizState  = quizState,
            userState  = userState
        )
    }
}

// --- Pure UI, all logic lifted out ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenUI(
    state: MainScreenState,
    navController: NavController,
    navigateTo: (Route) -> Unit,
    loadQuiz: (String) -> Unit,
    moveHome: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val activity = LocalActivity.current
    val lastBackPress = rememberSaveable { mutableLongStateOf(0L) }


    BackHandler {
        if (state.pagerState.currentPage != 0) {
            scope.launch { state.pagerState.scrollToPage(0) }
        } else {
            val now = System.currentTimeMillis()
            SnackBarManager.showSnackBar(R.string.press_back_again_to_exit, ToastType.INFO)
            if (now - lastBackPress.longValue < 3000) {
                activity?.finish()
            }
            lastBackPress.longValue = now
        }
    }

    Scaffold(
        topBar    = {
            MainActivityTopbar(
                navController = navController,
                onTopBarProfileClick = {
                    if (state.userState.isLoggedIn) {
                        scope.launch {
                            state.pagerState.scrollToPage(3)
                        }
                    } else {
                        navController.navigate(Route.Login) {
                            launchSingleTop = true
                        }
                    }
                },
                userData = state.userState.data,
                resetHome = moveHome
            )
        },
        bottomBar = {
            MainActivityBottomBar(state.pagerState.currentPage,
                { scope.launch { state.pagerState.scrollToPage(it) } },
                { navigateTo(Route.CreateQuizLayout) }) }
    ) { padding ->
        HorizontalPager(
            state = state.pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { page ->
            when (page) {
                0 -> HomePage(state.quizState.cards, loadQuiz, navController)
                1 -> TrendsPage(state.quizState.trends, loadQuiz)
                2 -> RanksPage(state.quizState.ranks)
                3 -> SettingsPage(
                    userData = state.userState.data,
                    isLoggedIn = state.userState.isLoggedIn,
                    navigateTo
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenUI(){
    val pagerState = rememberPagerState(
        initialPage = 0
    ) { 4 }

    val quizState = remember {
        QuizState(
            cards  = sampleQuizCardsWithTagList,
            trends = sampleQuizCardList,
            ranks  = sampleUserRankList
        )
    }

    val userState = remember {
        UserState(
            data       = sampleUserData,
            isLoggedIn = true
        )
    }

    val state = MainScreenState(
        pagerState = pagerState,
        quizState = quizState,
        userState =userState
    )
    val navController = rememberNavController()

    QuizzerAndroidTheme {
        MainScreenUI(
            state = state,
            navController = navController,
            navigateTo = {},
            loadQuiz = {},
            moveHome = {},
        )
    }
}
