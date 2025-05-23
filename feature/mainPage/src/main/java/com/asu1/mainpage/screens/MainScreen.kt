package com.asu1.mainpage.screens

import SnackBarManager
import ToastType
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.asu1.activityNavigation.Route
import com.asu1.appdatamodels.SettingItems
import com.asu1.customComposable.dialog.DialogComposable
import com.asu1.mainpage.composables.InquiryBottomSheetContent
import com.asu1.mainpage.composables.MainActivityBottomBar
import com.asu1.mainpage.composables.MainActivityTopbar
import com.asu1.mainpage.composables.SignoutBottomSheetContent
import com.asu1.mainpage.composables.UserRankComposableList
import com.asu1.mainpage.viewModels.InquiryViewModel
import com.asu1.mainpage.viewModels.QuizCardMainViewModel
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.quizcard.cardBase.VerticalQuizCardLargeColumn
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
import com.asu1.utils.setTopBarColor
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    quizCardMainViewModel: QuizCardMainViewModel = viewModel(),
    inquiryViewModel: InquiryViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    navigateTo: (Route) -> Unit = { },
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
        SnackBarManager.showSnackBar(R.string.press_back_again_to_exit, ToastType.INFO)
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
                onTopBarProfileClick = {
                    if (isLoggedIn) {
                        updateSelectedTab(3)
                    } else {
                        navController.navigate(Route.Login) {
                            launchSingleTop = true
                        }
                    }
                },
                userData = userData,
                resetHome = moveHome,
            )
        },
        bottomBar = {
            MainActivityBottomBar(
                bottomBarSelection = pagerState.currentPage,

                setSelectedTab ={updateSelectedTab(it)},
                navigateToCreateQuizLayout = {navigateTo(Route.CreateQuizLayout)}
            )
        },
        content = { paddingValues ->
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
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
                        var showInquiry by remember { mutableStateOf(false) }
                        var showSignOut by remember { mutableStateOf(false) }
                        if(showInquiry) {
                            ModalBottomSheet(onDismissRequest = {showInquiry = false },
                                modifier = Modifier.imePadding()
                            ) {
                                InquiryBottomSheetContent(
                                    onDismissRequest = { showInquiry = false
                                    },
                                    userData = userData,
                                    isDone = false,
                                    onSendInquiry = { email, type, text ->
                                        inquiryViewModel.sendInquiry(email, type, text)
                                        showInquiry = false
                                    }
                                )
                            }
                        }
                        else if(showSignOut && isLoggedIn) {
                            ModalBottomSheet(onDismissRequest = {showSignOut = false },
                                modifier = Modifier.imePadding()) {
                                SignoutBottomSheetContent(
                                    onDismissRequest = { showSignOut = false
                                        userViewModel.logOut()
                                    },
                                    userData = userData,
                                    isDone = false,
                                    onSendSignOut = { email ->
                                        userViewModel.signOut(email)
                                        showSignOut = false
                                    }
                                )
                            }
                        }

                        UserSettingsScreen(
                            settingItems =
                                remember{
                                    persistentListOf(
                                        SettingItems(
                                            stringResourceId = R.string.my_quizzes,
                                            vectorIcon = Icons.AutoMirrored.Filled.List,
                                            onClick = {navigateTo(Route.LoadUserQuiz)}
                                        ),
                                        SettingItems(
                                            stringResourceId = R.string.my_activities,
                                            vectorIcon = Icons.Default.BarChart,
                                            onClick = {navigateTo(Route.MyActivities)}
                                        ),
                                        SettingItems(
                                            stringResourceId = R.string.notification,
                                            vectorIcon = Icons.Default.Notifications,
                                            onClick = {navigateTo(Route.Notifications)}
                                        ),
                                        SettingItems(
                                            stringResourceId = R.string.inquiry,
                                            vectorIcon = Icons.AutoMirrored.Filled.HelpOutline,
                                            onClick = {showInquiry = true}
                                        ),
                                    )
                                },
                            isLoggedIn = isLoggedIn,
                            userData = userData,
                            logOut = { userViewModel.logOut() },
                            onSignOut = { showSignOut = true },
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun UserProfilePic(userData: UserViewModel.UserData?, onClick: () -> Unit = {}, modifier: Modifier = Modifier,
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