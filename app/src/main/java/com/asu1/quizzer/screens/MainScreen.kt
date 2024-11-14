package com.asu1.quizzer.screens

import HorizontalQuizCardItemLarge
import HorizontalQuizCardItemVertical
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.DialogComposable
import com.asu1.quizzer.states.LoginActivityState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.InquiryViewModel
import com.asu1.quizzer.viewModels.QuizCardMainViewModel
import com.asu1.quizzer.viewModels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavController,
    quizCardMainViewModel: QuizCardMainViewModel = viewModel(),
    inquiryViewModel: InquiryViewModel = viewModel(),
    loginActivityState: LoginActivityState,
    navigateToQuizLayoutBuilder: () -> Unit = {},
    navigateToMyQuizzes: () -> Unit = {},
    testPress: () -> Unit = {},
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val quizCards by quizCardMainViewModel.quizCards.collectAsState()
    var backPressedTime by remember { mutableStateOf(0L) }
    val userData by loginActivityState.userData
    val isUserLoggedIn by loginActivityState.isUserLoggedIn
    val bottomBarSelection by quizCardMainViewModel.bottomBarSelection.observeAsState(0)

    LaunchedEffect(Unit){
//        quizCardMainViewModel.fetchQuizCards("ko")
    }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        val toast = Toast.makeText(context, "Press back again to exit", Toast.LENGTH_LONG)
        if (currentTime - backPressedTime < 3000) {
            // Terminate the app
            toast.cancel()
            (context as? Activity)?.finish()
        } else {
            backPressedTime = currentTime
            toast.show()

            scope.launch {
                delay(3000)
                toast.cancel()
                backPressedTime = 0L
            }
        }
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
                    logOut = { loginActivityState.logout() },
                    signOut = { email -> loginActivityState.signout(email) },
                    navigateToMyQuizzes = { navigateToMyQuizzes() }
                )
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Scaffold(
                        topBar = {
                            MainActivityTopbar(navController,
                                { openDrawer() }, isUserLoggedIn, userData)
                        },
                        bottomBar = {
                            MainActivityBottomBar({openDrawer()}, bottomBarSelection = bottomBarSelection,
                                navigateToQuizLayoutBuilder = navigateToQuizLayoutBuilder,
                                testPress = testPress)
                        },
                        content = { paddingValues ->
                            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                                item {
                                    Text(
                                        text = quizCards.quizCards1.tag,
                                        modifier = Modifier.padding(start = 16.dp, top= 8.dp),
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    HorizontalQuizCardItemLarge(quizCards = quizCards.quizCards1.quizCards)
                                }
                                item {
                                    Text(
                                        text = quizCards.quizCards2.tag,
                                        modifier = Modifier.padding(start = 16.dp, top= 8.dp),
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    HorizontalQuizCardItemVertical(quizCards = quizCards.quizCards2.quizCards)
                                }
                                item {
                                    Text(
                                        text = quizCards.quizCards3.tag,
                                        modifier = Modifier.padding(start = 16.dp, top= 8.dp),
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    HorizontalQuizCardItemVertical(quizCards = quizCards.quizCards3.quizCards)
                                }
                                item{
                                    Spacer(modifier = Modifier.size(16.dp))
                                    PrivacyPolicyRow(navController)
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityTopbar(navController: NavController, openDrawer: () -> Unit = {}, isLoggedIn: Boolean, userData: UserViewModel.UserDatas?) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text("Quizzer") },
        navigationIcon = {
            IconButton(onClick = {  }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "App Icon"
                )
            }
        },
        actions = {
            IconButton(onClick = { moveToSearchActivity(navController) }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            UserProfilePic(userData, onClick = {
                scope.launch {
                    if(isLoggedIn){
                        openDrawer()
                    }
                    else{
                        NavMultiClickPreventer.navigate(navController, Route.Login)
                    }
                }
            })
        },
    )
}

@Preview
@Composable
fun MainActivityTopbarPreview(){
    QuizzerAndroidTheme {
        MainActivityTopbar(
            navController = rememberNavController(),
            isLoggedIn = true,
            userData = userDataTest,
        )
    }
}

@Composable
fun MainActivityBottomBar(onDrawerOpen: () -> Unit = {}, bottomBarSelection: Int = 0,
                          navigateToQuizLayoutBuilder: () -> Unit = {},
                          testPress: () -> Unit = {}) {
    val defaultIconSize = 24.dp

    BottomAppBar(
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { /* Handle Home click */ },
                    modifier = Modifier.weight(1f),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(defaultIconSize))
                }
                IconButton(
                    onClick = {
                        testPress()
                    },
                    modifier = Modifier.weight(1f),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = "Trends", modifier = Modifier.size(defaultIconSize))
                }
                IconButton(
                    onClick = { navigateToQuizLayoutBuilder() },
                    modifier = Modifier.weight(1.5f).testTag("MainScreenCreateQuiz"),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.Default.AddCircleOutline, contentDescription = "Create Quiz", modifier = Modifier.size(1.5f * defaultIconSize))
                }
                IconButton(
                    onClick = { /* Handle Stats click */ },
                    modifier = Modifier.weight(1f),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.Default.BarChart, contentDescription = "Stats", modifier = Modifier.size(defaultIconSize))
                }
                IconButton(
                    onClick = {
                        onDrawerOpen()
                    },
                    modifier = Modifier.weight(1f),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 4) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", modifier = Modifier.size(defaultIconSize))
                }
            }
        }
    )
}

@Preview
@Composable
fun MainActivityBottomBarPreview(){
    QuizzerAndroidTheme {
        MainActivityBottomBar(
        )
    }
}

fun moveToSearchActivity(navController: NavController, searchText: String = "") {
    NavMultiClickPreventer.navigate(navController, Route.Search(searchText))
}

@Composable
fun PrivacyPolicyRow(navController: NavController) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(onClick = {
            NavMultiClickPreventer.navigate(navController, Route.PrivacyPolicy)
        }) {
            Text(stringResource(R.string.privacy_policy))
        }
        Text(
            text = stringResource(R.string.contact) + ": whwkd122@gmail.com",
            modifier = Modifier.padding(start = 16.dp),
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
                Box(modifier = Modifier.size(iconSize)) {
                    Image(
                        painter = rememberAsyncImagePainter(model = urlToImage),
                        contentDescription = "User Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(navController: NavController, closeDrawer: () -> Unit = {},
                  userData: UserViewModel.UserDatas?,
                  onSendInquiry: (String, String, String) -> Unit = { _, _, _ -> },
                  logOut: () -> Unit = { },
                  signOut: (String) -> Unit = { },
                  navigateToMyQuizzes: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val nickname = userData?.nickname
    var showInquiry by remember { mutableStateOf(false) }
    var showSignOut by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val isUserLoggedIn = userData != null

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        if(showInquiry) {
            ModalBottomSheet(onDismissRequest = {showInquiry = false },
                modifier = Modifier.imePadding()
            ) {
                InquiryBottomSheetContent(
                    onDismissRequest = { showInquiry = false
                        closeDrawer() },
                    userData = userData,
                    isDone = false,
                    onSendInquiry = { email, type, text ->
                        onSendInquiry(email, type, text)
                        showInquiry = false
                    }
                )
            }
        }
        else if(showSignOut && isUserLoggedIn) {
            ModalBottomSheet(onDismissRequest = {showSignOut = false },
                modifier = Modifier.imePadding()) {
                SignoutBottomSheetContent(
                    onDismissRequest = { showSignOut = false
                        logOut()
                        closeDrawer() },
                    userData = userData,
                    isDone = false,
                    onSendSignOut = { email ->
                        signOut(email)
                        showSignOut = false
                    }
                )
            }
        }
        else if(showLogoutDialog){
            LogoutConfirmationDialog(
                onConfirm = {
                    logOut()
                    showLogoutDialog = false
                    closeDrawer()
                },
                onDismiss = { showLogoutDialog = false }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(250.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Row(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    UserProfilePic(userData, onClick = {
                        scope.launch {
                            if (isUserLoggedIn) {
                                //TODO LATER for profile pic fix and user tags setting
                            } else {
                                NavMultiClickPreventer.navigate(navController, Route.Login)
                            }
                        }
                    })
                    Text(nickname ?: "Guest", modifier = Modifier.padding(16.dp))
                    IconButton(onClick = {
                        if (isUserLoggedIn) {
                            showLogoutDialog = true
                        } else {
                            NavMultiClickPreventer.navigate(navController, Route.Login)
                        }
                    }) {
                        Icon(
                            imageVector = if (isUserLoggedIn) Icons.AutoMirrored.Filled.Logout else Icons.AutoMirrored.Filled.Login,
                            contentDescription = if (isUserLoggedIn) "Logout" else "Login"
                        )
                    }

                }
                Row(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    UserProfilePic(userData, onClick = {
                        scope.launch {
                            if (isUserLoggedIn) {
                                //TODO LATER for profile pic fix and user tags setting
                            } else {
                                NavMultiClickPreventer.navigate(navController, Route.Login)
                            }
                        }
                    })
                    Text(nickname ?: "Guest", modifier = Modifier.padding(16.dp))
                    IconButton(onClick = {
                        if (isUserLoggedIn) {
                            showLogoutDialog = true
                        } else {
                            NavMultiClickPreventer.navigate(navController, Route.Login)
                        }
                    }) {
                        Icon(
                            imageVector = if (isUserLoggedIn) Icons.AutoMirrored.Filled.Logout else Icons.AutoMirrored.Filled.Login,
                            contentDescription = if (isUserLoggedIn) "Logout" else "Login"
                        )
                    }

                }
            }
            Box(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    if(isUserLoggedIn) {
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navigateToMyQuizzes()
                                closeDrawer()
                            },
                        )
                        {
                            Text(
                                "My Quizzes",
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {},
                        ) {
                            Text(
                                "Profile",
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                style = TextStyle(
                                    textDecoration = TextDecoration.LineThrough,
                                )
                            )
                        }
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {},
                        ) {
                            Text(
                                "Settings",
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                style = TextStyle(
                                    textDecoration = TextDecoration.LineThrough,
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showInquiry = true
                        },
                    ) {
                        Text(
                            stringResource(R.string.inquiry),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    if (isUserLoggedIn) {
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                showSignOut = true
                            },
                        ) {
                            Text(
                                stringResource(R.string.sign_out),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
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

@Composable
fun SignoutBottomSheetContent(
    onDismissRequest: () -> Unit,
    userData: UserViewModel.UserDatas?,
    isDone: Boolean = false,
    onSendSignOut: (String) -> Unit = { },
){
    var textFieldValue by remember { mutableStateOf("") }
    val email = userData?.email ?: "GUEST"

    LaunchedEffect(isDone) {
        if (isDone) {
            onDismissRequest()
        }
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(stringResource(id = R.string.sign_out), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text("If you sign out, all your activities and records will be deleted. Even if you re-register, data won't be restored", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("Enter \"Sign Out\"") },
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = {
            if(textFieldValue == "Sign Out" || textFieldValue == "회원 탈퇴") {
                onSendSignOut(email)
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun InquiryBottomSheetContent(
    onDismissRequest: () -> Unit,
    userData: UserViewModel.UserDatas?,
    isDone: Boolean,
    onSendInquiry: (String, String, String) -> Unit
) {
    val email = userData?.email ?: "GUEST"
    val options = listOf("Bug Report", "Quiz Report", "Develop Plan Request", "Others")
    var selectedOption by remember { mutableStateOf(options[0]) }
    var textFieldValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(isDone) {
        if (isDone) {
            onDismissRequest()
        }
    }

    Column(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        Text(stringResource(id = R.string.inquiry), style = MaterialTheme.typography.headlineSmall)
        Text(stringResource(R.string.inquiry_body), style = MaterialTheme.typography.bodySmall)
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedOption)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon"
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(text = option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("Enter text") },
            minLines = 1,
            maxLines = 5,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = {
                onSendInquiry(email, selectedOption, textFieldValue)
            })
        )
        TextButton(onClick = {
            onSendInquiry(email, selectedOption, textFieldValue) },
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text("Submit")
        }
    }

}

val userDataTest = UserViewModel.UserDatas("whwkd122@gmail.com", "whwkd122", null, setOf("tag1", "tag2"))


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    QuizzerAndroidTheme {
        MainScreen(
            navController,
            loginActivityState = getLoginActivityState(),
        )
    }
}

@Preview
@Composable
fun DrawerPreview(){
    val navController = rememberNavController()
    QuizzerAndroidTheme {
        DrawerContent(
            navController = navController,
            closeDrawer = {},
            userData = userDataTest,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignoutBottomSheetContentPreview() {
    QuizzerAndroidTheme {
        SignoutBottomSheetContent(
            onDismissRequest = { },
            userData = userDataTest,
            isDone = false,
            onSendSignOut = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InquiryBottomSheetContentPreview() {
    QuizzerAndroidTheme {
        InquiryBottomSheetContent(
            onDismissRequest = { },
            userData = userDataTest,
            isDone = false,
            onSendInquiry = { _, _, _ -> }
        )
    }
}