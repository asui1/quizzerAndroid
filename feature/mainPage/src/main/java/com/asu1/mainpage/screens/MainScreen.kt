package com.asu1.mainpage.screens

import SnackBarManager
import ToastType
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.dialog.DialogComposable
import com.asu1.mainpage.composables.MainActivityBottomBar
import com.asu1.mainpage.composables.MainActivityTopbar
import com.asu1.mainpage.viewModels.QuizCardViewModel
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.QuizCardsWithTag
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
import com.asu1.resources.UserBackground1
import com.asu1.resources.UserBackground2
import com.asu1.resources.UserBackground3
import com.asu1.resources.UserBackground4
import com.asu1.resources.UserBackground5
import com.asu1.resources.UserBackground6
import com.asu1.resources.UserBackground7
import com.asu1.resources.UserBackground8
import com.asu1.userdatamodels.UserRank
import kotlinx.coroutines.launch
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    navigateTo: (Route) -> Unit,
    loadQuiz: (String) -> Unit,
    loadQuizResult: (String) -> Unit,
    moveHome: () -> Unit
) {
    // 1) collect all state and effects into one state holder
    val state = rememberMainScreenState(
        quizCardViewModel = viewModel(),
        userViewModel         = viewModel(),
        loadQuiz = loadQuiz,
        loadQuizResult = loadQuizResult,
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
    loadQuiz: (String) -> Unit,
    loadQuizResult: (String) -> Unit,
): MainScreenState {
    // 1) your pager
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })

    // 2) collect all quiz‐related bits
    val quizCards    by quizCardViewModel.quizCards.collectAsStateWithLifecycle()
    val quizTrends   by quizCardViewModel.visibleQuizTrends.collectAsStateWithLifecycle()
    val userRanks    by quizCardViewModel.visibleUserRanks.collectAsStateWithLifecycle()

    // 3) collect all user‐related bits
    val userData   by userViewModel.userData.observeAsState()
    val isLoggedIn by userViewModel.isUserLoggedIn.observeAsState(false)

    // 4) triggers to load detail screens
    val loadQuizId   by quizCardViewModel.loadQuizId.observeAsState()
    val loadResultId by quizCardViewModel.loadResultId.observeAsState()

    // 5) launch the side‐effects
    LaunchedEffect(loadQuizId)   { loadQuizId?.let { loadQuiz(it) } }
    LaunchedEffect(loadResultId) { loadResultId?.let { loadQuizResult(it) } }

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

@Composable
fun UserProfilePic(
    modifier: Modifier = Modifier,
    userData: UserViewModel.UserData?,
    onClick: () -> Unit = {},
    iconSIze: Dp = 30.dp) {
    val urlToImage = userData?.urlToImage
    IconButton(onClick = onClick, modifier = modifier) {
        if (userData != null) {
            UriImageButton(modifier = Modifier
                .size(iconSIze)
                .clip(shape = RoundedCornerShape(8.dp)),
                urlToImage = urlToImage,
                nickname = userData.nickname?.get(0) ?: 'O'
            )
        }
    }
}

@Composable
fun UriImageButton(modifier: Modifier = Modifier, urlToImage: String?, nickname: Char = 'Q') {
    val painter = rememberAsyncImagePainter(
        model = urlToImage,
    )
    val isError = painter.state is AsyncImagePainter.State.Error
    val randomInt = Random.nextInt(0, 8)
    val backgroundColor = remember{
        when(randomInt){
            0 -> UserBackground1
            1 -> UserBackground2
            2 -> UserBackground3
            3 -> UserBackground4
            4 -> UserBackground5
            5 -> UserBackground6
            6 -> UserBackground7
            7 -> UserBackground8
            else -> UserBackground1
        }
    }

    Box(modifier = modifier,
        contentAlignment = Alignment.Center) {
        if (isError) {
            BoxWithTextAndColorBackground(backgroundColor, nickname)
        } else {
            Image(
                painter = painter,
                contentDescription = "User Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun BoxWithTextAndColorBackground(backgroundColor: Color, nickname: Char, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = nickname.toString(),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun UserProfilePicPreview(){
    QuizzerAndroidTheme {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(30.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        ) {
            BoxWithTextAndColorBackground(
                UserBackground1, '글',
            )
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    DialogComposable(
        title = R.string.logout_confirmation,
        message = R.string.logout_confirmation_body,
        onContinue = { onConfirm() },
        onContinueText = R.string.logout_confirm,
        onCancel = onDismiss,
        onCancelText = R.string.logout_cancel
    )
}

@Preview
@Composable
fun LogoutConfirmationDialogPreview(
) {
    QuizzerAndroidTheme {
        LogoutConfirmationDialog(
            onConfirm = { },
            onDismiss = { }
        )
    }
}
