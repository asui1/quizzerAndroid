package com.asu1.quizzer.screens.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.DialogComposable
import com.asu1.quizzer.composables.UserRankComposableList
import com.asu1.quizzer.composables.mainscreen.DrawerContent
import com.asu1.quizzer.composables.mainscreen.MainActivityBottomBar
import com.asu1.quizzer.composables.mainscreen.MainActivityTopbar
import com.asu1.quizzer.composables.quizcards.VerticalQuizCardLargeColumn
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.ui.theme.UserBackground1
import com.asu1.quizzer.ui.theme.UserBackground2
import com.asu1.quizzer.ui.theme.UserBackground3
import com.asu1.quizzer.ui.theme.UserBackground4
import com.asu1.quizzer.ui.theme.UserBackground5
import com.asu1.quizzer.ui.theme.UserBackground6
import com.asu1.quizzer.ui.theme.UserBackground7
import com.asu1.quizzer.ui.theme.UserBackground8
import com.asu1.quizzer.util.setTopBarColor
import com.asu1.quizzer.viewModels.InquiryViewModel
import com.asu1.quizzer.viewModels.QuizCardMainViewModel
import com.asu1.quizzer.viewModels.UserViewModel
import kotlinx.coroutines.launch
import java.util.Locale
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val quizCards by quizCardMainViewModel.quizCards.collectAsStateWithLifecycle()
    val quizTrends by quizCardMainViewModel.visibleQuizTrends.collectAsStateWithLifecycle()
    val userRanks by quizCardMainViewModel.visibleUserRanks.collectAsStateWithLifecycle()
    val userData by userViewModel.userData.observeAsState()
    val isUserLoggedIn by userViewModel.isUserLoggedIn.observeAsState(false)
    val lang = remember{if(Locale.getDefault().language == "ko") "ko" else "en"}
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 3 },
    )
    val coroutineScope = rememberCoroutineScope()
    val loadQuizId by quizCardMainViewModel.loadQuizId.observeAsState()
    val loadResultId by quizCardMainViewModel.loadResultId.observeAsState()
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val view = LocalView.current
    val scope = rememberCoroutineScope()


    LaunchedEffect(loadQuizId){
        if(loadQuizId != null){
            loadQuiz(loadQuizId!!)
        }
    }

    LaunchedEffect(loadResultId){
        if(loadResultId != null){
            loadQuizResult(loadResultId!!)
        }
    }

    LaunchedEffect(Unit){
        setTopBarColor(
            view = view,
            color = primaryContainer
        )
    }

    fun updateSelectedTab(index: Int) {
        coroutineScope.launch {
            pagerState.scrollToPage(index)
        }
        quizCardMainViewModel.tryUpdate(index, language = lang)
    }

    fun openDrawer() {
        scope.launch {
            drawerState.open()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(navController, closeDrawer = { scope.launch { drawerState.close() } },
                    userData = userData,
                    onSendInquiry = { email, type, text -> inquiryViewModel.sendInquiry(email, type, text) },
                    logOut = { userViewModel.logOut() },
                    signOut = { email -> userViewModel.signout(email) },
                    navigateToMyQuizzes = { navigateToMyQuizzes() }
                )
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Scaffold(
                        topBar = {
                            MainActivityTopbar(navController,
                                { openDrawer() }, isUserLoggedIn, userData,
                                resetHome = moveHome)
                        },
                        bottomBar = {
                            MainActivityBottomBar({openDrawer()}, bottomBarSelection = pagerState.currentPage,
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
                                            isKo = lang == "ko",
                                            navController = navController,
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
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun UserProfilePic(userData: UserViewModel.UserDatas?, onClick: () -> Unit = {}) {
    val isUserLoggedIn = userData != null
    val urlToImage = userData?.urlToImage
    val iconSize = 30.dp

    if (isUserLoggedIn) {
        if(urlToImage != null) {
            IconButton(onClick = onClick) {
                UriImageButton(modifier = Modifier
                    .size(iconSize)
                    .clip(shape = RoundedCornerShape(8.dp)), urlToImage,
                    nickname = userData.nickname?.get(0) ?: 'Q'
                )
            }
        } else {
            IconButton(onClick = onClick) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "User Image",
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    } else {
        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Login",
                modifier = Modifier.size(iconSize)
            )
        }

    }
}

@Composable
fun UriImageButton(modifier: Modifier = Modifier, urlToImage: String, nickname: Char = 'Q') {
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

    Box(modifier = modifier) {
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
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            text = nickname.toString(),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun UserProfilePicPreview(){
    QuizzerAndroidTheme {

        Box(modifier = Modifier
            .size(30.dp)
            .clip(shape = RoundedCornerShape(8.dp))) {
            BoxWithTextAndColorBackground(
                UserBackground1, 'A',
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
        titleResource = R.string.logout_confirmation,
        messageResource = R.string.logout_confirmation_body,
        onContinue = {onConfirm()},
        onContinueResource = R.string.logout_confirm,
        onCancel = onDismiss,
        onCancelResource = R.string.logout_cancel
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