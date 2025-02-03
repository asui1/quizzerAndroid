package com.asu1.quizzer.screens.mainScreen

import ToastManager
import ToastType
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.asu1.customdialogs.DialogComposable
import com.asu1.quizcard.VerticalQuizCardLargeColumn
import com.asu1.quizzer.composables.UserRankComposableList
import com.asu1.quizzer.composables.mainscreen.MainActivityBottomBar
import com.asu1.quizzer.composables.mainscreen.MainActivityTopbar
import com.asu1.quizzer.composables.mainscreen.UserSettings
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.setTopBarColor
import com.asu1.quizzer.viewModels.InquiryViewModel
import com.asu1.quizzer.viewModels.QuizCardMainViewModel
import com.asu1.quizzer.viewModels.UserViewModel
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R
import com.asu1.resources.UserBackground1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


@Composable
fun MainScreen(
    navController: NavController,
    quizCardMainViewModel: QuizCardMainViewModel = viewModel(),
    inquiryViewModel: InquiryViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    navigateToQuizLayoutBuilder: () -> Unit = {},
    navigateToMyQuizzes: () -> Unit = {},
    loadQuiz: (String) -> Unit = { },
    loadQuizResult: (String) -> Unit = { },
    moveHome: () -> Unit = {},
) {
    val quizCards by quizCardMainViewModel.quizCards.collectAsStateWithLifecycle()
    val quizTrends by quizCardMainViewModel.visibleQuizTrends.collectAsStateWithLifecycle()
    val userRanks by quizCardMainViewModel.visibleUserRanks.collectAsStateWithLifecycle()
    val userData by userViewModel.userData.observeAsState()
    val isLoggedIn by userViewModel.isUserLoggedIn.observeAsState(false)
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 4 },
    )
    val coroutineScope = rememberCoroutineScope()
    val loadQuizId by quizCardMainViewModel.loadQuizId.observeAsState()
    val loadResultId by quizCardMainViewModel.loadResultId.observeAsState()
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current

    fun updateSelectedTab(index: Int) {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                pagerState.scrollToPage(index)
            }
            quizCardMainViewModel.tryUpdate(index)
        }
    }

    BackHandler(
    ) {
        if(pagerState.currentPage != 0){
            updateSelectedTab(0)
            return@BackHandler
        }
        val currentTime = System.currentTimeMillis()
        ToastManager.showToast(R.string.press_back_again_to_exit, ToastType.INFO)
        if (currentTime - backPressedTime < 3000) {
            (context as? Activity)?.finish()
        } else {
            backPressedTime = currentTime
            scope.launch {
                delay(3000)
                backPressedTime = 0L
            }
        }
    }

    LaunchedEffect(loadQuizId) {
        withContext(Dispatchers.Main) {
            if (loadQuizId != null) {
                loadQuiz(loadQuizId!!)
            }
        }
    }

    LaunchedEffect(loadResultId) {
        withContext(Dispatchers.Main) {
            if (loadResultId != null) {
                loadQuizResult(loadResultId!!)
            }
        }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main) {
            setTopBarColor(
                view = view,
                color = primaryContainer
            )
        }
    }

    Scaffold(
        topBar = {
            MainActivityTopbar(
                navController,
                onTopbarProfileClick = {
                    if(isLoggedIn){
                        updateSelectedTab(3)
                    }else{
                        navController.navigate(Route.Login){
                            launchSingleTop = true
                        }
                    }
                },
                userData,
                resetHome = moveHome
            )
        },
        bottomBar = {
            MainActivityBottomBar(
                bottomBarSelection = pagerState.currentPage,
                navigateToQuizLayoutBuilder = navigateToQuizLayoutBuilder,
                setSelectedTab ={updateSelectedTab(it)})
        },
        content = { paddingValues ->
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            ){page ->
                when(page){
                    0 -> {
                        HomeScreen(
                            quizCards = quizCards,
                            loadQuiz = loadQuiz,
                            moveToPrivacyPolicy = {
                                navController.navigate(Route.PrivacyPolicy){
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                    1 -> {
                        VerticalQuizCardLargeColumn(
                            quizCards = quizTrends,
                            onClick = loadQuiz,
                            getMoreTrends = {
                                quizCardMainViewModel.getMoreQuizTrends()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    2 -> {
                        UserRankComposableList(
                            userRanks = userRanks,
                            getMoreUserRanks = {
                                quizCardMainViewModel.getMoreUserRanks()
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    3 ->{
                        UserSettings(
                            isLoggedIn = isLoggedIn,
                            userData = userData,
                            onSendInquiry = { email, type, text -> inquiryViewModel.sendInquiry(email, type, text) },
                            logOut = { userViewModel.logOut() },
                            signOut = { email -> userViewModel.signout(email) },
                            navigateToMyQuizzes = { navigateToMyQuizzes() }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun UserProfilePic(userData: UserViewModel.UserDatas?, onClick: () -> Unit = {}, modifier: Modifier = Modifier,
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
            1 -> com.asu1.resources.UserBackground2
            2 -> com.asu1.resources.UserBackground3
            3 -> com.asu1.resources.UserBackground4
            4 -> com.asu1.resources.UserBackground5
            5 -> com.asu1.resources.UserBackground6
            6 -> com.asu1.resources.UserBackground7
            7 -> com.asu1.resources.UserBackground8
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