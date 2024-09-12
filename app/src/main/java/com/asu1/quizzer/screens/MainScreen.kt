package com.asu1.quizzer.screens

import HorizontalQuizCardItemLarge
import HorizontalQuizCardItemVertical
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.asu1.quizzer.R
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.composables.DialogComposable
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.states.MainActivityState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.viewModels.QuizCardMainViewModel
import com.asu1.quizzer.viewModels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import loadImageAsByteArray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, mainActivityState: MainActivityState) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val quizCards by mainActivityState.quizCards
    var backPressedTime by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit){
//        mainActivityState.updateQuizCards()
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

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                if (drawerState.isOpen) {
                    DrawerContent(navController, closeDrawer = { scope.launch { drawerState.close() } }, mainActivityState = mainActivityState)
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Scaffold(
                        topBar = {
                            MainActivityTopbar(navController, mainActivityState, drawerState)
                        },
                        bottomBar = {
                            MainActivityBottomBar(navController, mainActivityState, drawerState)
                        },
                        content = { paddingValues ->
                            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                                //TODO : Add Search Bar
                                if (quizCards != null) {
                                    item {
                                        Text(
                                            text = quizCards!!.quizCards1.tag,
                                            modifier = Modifier.padding(start = 16.dp, top= 8.dp),
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                        HorizontalQuizCardItemLarge(quizCards = quizCards!!.quizCards1.quizCards)
                                    }
                                    item {
                                        Text(
                                            text = quizCards!!.quizCards2.tag,
                                            modifier = Modifier.padding(start = 16.dp, top= 8.dp),
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                        HorizontalQuizCardItemVertical(quizCards = quizCards!!.quizCards2.quizCards)
                                    }
                                    item {
                                        Text(
                                            text = quizCards!!.quizCards3.tag,
                                            modifier = Modifier.padding(start = 16.dp, top= 8.dp),
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                        HorizontalQuizCardItemVertical(quizCards = quizCards!!.quizCards3.quizCards)
                                    }
                                    item{
                                        Spacer(modifier = Modifier.size(16.dp))
                                        PrivacyPolicyRow(navController)
                                    }
                                } else {
                                    item {
                                        Text("Loading...")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityTopbar(navController: NavController, mainActivityState: MainActivityState, drawerState: DrawerState) {
    val isLoggedIn by mainActivityState.isLoggedIn
    val userData by mainActivityState.userData
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
                        drawerState.open()
                    }
                    else{
                        navController.navigate(Route.Login)
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
            mainActivityState = provideMainActivityStateTest(),
            drawerState = rememberDrawerState(DrawerValue.Closed)
        )
    }
}

@Composable
fun MainActivityBottomBar(navController: NavController, mainActivityState: MainActivityState, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val bottomBarSelection by mainActivityState.bottomBarSelection
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
                    onClick = { /* Handle Trends click */ },
                    modifier = Modifier.weight(1f),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (bottomBarSelection == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = "Trends", modifier = Modifier.size(defaultIconSize))
                }
                IconButton(
                    onClick = { navController.navigate(Route.CreateQuizLayout) },
                    modifier = Modifier.weight(1.5f),
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
                        scope.launch {
                            drawerState.open()
                        }
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
            navController = rememberNavController(),
            mainActivityState = provideMainActivityStateTest(),
            drawerState = rememberDrawerState(DrawerValue.Closed)
        )
    }
}

fun moveToSearchActivity(navController: NavController) {
    navController.navigate(Route.Search)
}

@Composable
fun PrivacyPolicyRow(navController: NavController) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(onClick = {
            navController.navigate(Route.PrivacyPolicy)
        }) {
            Text(stringResource(R.string.privacy_policy))
        }
        Text(
            text = stringResource(R.string.contact) + ": whwkd122@gmail.com",
            modifier = Modifier.padding(start = 16.dp),
        )
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun UserProfilePic(userData: UserViewModel.UserDatas?, onClick: () -> Unit = {}) {
    val isUserLoggedIn = userData != null
    val urlToImage = userData?.urlToImage

    if (isUserLoggedIn) {
        if(urlToImage != null) {
            IconButton(onClick = onClick) {
                Box(modifier = Modifier.size(40.dp)) {
                    Image(
                        painter = rememberImagePainter(data = urlToImage),
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
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    } else {
        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Login",
                modifier = Modifier.size(40.dp)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DrawerContent(navController: NavController, closeDrawer: () -> Unit = {}, mainActivityState: MainActivityState) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userData by mainActivityState.userData
    val isUserLoggedIn by mainActivityState.isLoggedIn
    val nickname = userData?.nickname
    var showInquiry by remember { mutableStateOf(false) }
    var showSignOut by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        if(showInquiry) {
            ModalBottomSheet(onDismissRequest = {showInquiry = false },
                modifier = Modifier.imePadding()
            ) {
                InquiryBottomSheetContent(
                    onDismissRequest = { showInquiry = false
                        closeDrawer() },
                    mainActivityState = mainActivityState
                )
            }
        }
        else if(showSignOut && isUserLoggedIn) {
            ModalBottomSheet(onDismissRequest = {showSignOut = false },
                modifier = Modifier.imePadding()) {
                SignoutBottomSheetContent(
                    mainActivityState,
                    onDismissRequest = { showSignOut = false
                        mainActivityState.logOut()
                        closeDrawer() },
                )
            }
        }
        else if(showLogoutDialog){
            LogoutConfirmationDialog(
                onConfirm = {
                    mainActivityState.logOut()
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
                                navController.navigate(Route.Login)
                            }
                        }
                    })
                    Text(nickname ?: "Guest", modifier = Modifier.padding(16.dp))
                    IconButton(onClick = {
                        if (isUserLoggedIn) {
                            showLogoutDialog = true
                        } else {
                            navController.navigate(Route.Login)
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
                Column(modifier = Modifier.padding(16.dp)) {
                    TextButton(onClick = {
                        showInquiry = true
                    },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            stringResource(R.string.inquiry),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    if (isUserLoggedIn) {
                        TextButton(onClick = {
                            showSignOut = true
                        },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                stringResource(R.string.sign_out),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(16.dp),
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
    mainActivityState: MainActivityState,
    onDismissRequest: () -> Unit,
){
    var textFieldValue by remember { mutableStateOf("") }
    val userData by mainActivityState.userData
    val email = userData?.email ?: "GUEST"
    val isDone by mainActivityState.isSignOutDone
    val onSendSignOut = mainActivityState.onSendSignOut

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
    mainActivityState: MainActivityState,
    onDismissRequest: () -> Unit,
) {
    val userData = mainActivityState.userData.value
    val email = userData?.email ?: "GUEST"
    val onSendInquiry = mainActivityState.onSendInquiry
    val isDone by mainActivityState.isInquiryDone
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


@Composable
fun provideMainActivityStateTest(): MainActivityState {
    val context = LocalContext.current
    val imageByte = loadImageAsByteArray(context, R.drawable.question2)
    val quizCard = QuizCard(
        id = "1",
        title = "Quiz 1",
        tags = listOf("tag1", "tag2"),
        creator = "Creator",
        image = imageByte,
        count = 0
    )
    return MainActivityState(
        bottomBarSelection = remember { mutableIntStateOf(0) },
        userData = remember { mutableStateOf(userDataTest) },
        quizCards = remember {
            mutableStateOf(
                QuizCardMainViewModel.QuizCards(
                    QuizCardMainViewModel.QuizCardsWithTag("Most Viewed", listOf(quizCard, quizCard, quizCard)),
                    QuizCardMainViewModel.QuizCardsWithTag("Similar Items", listOf(quizCard, quizCard, quizCard)),
                    QuizCardMainViewModel.QuizCardsWithTag("Recent Items", listOf(quizCard, quizCard, quizCard))
                )
            )
        },
        isSignOutDone = remember { mutableStateOf(false) },
        isInquiryDone = remember { mutableStateOf(false) },
        isLoggedIn = remember { mutableStateOf(true) },
        logOut = { },
        onSendSignOut = { },
        onSendInquiry = { _, _, _ -> },
        updateQuizCards = { }
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val mainActivityStateTest = provideMainActivityStateTest()
    val navController = rememberNavController()
    QuizzerAndroidTheme {
        MainScreen(
            navController,
            mainActivityStateTest,
        )
    }
}

@Preview
@Composable
fun DrawerPreview(){
    val navController = rememberNavController()
    val mainActivityStateTest = provideMainActivityStateTest()
    QuizzerAndroidTheme {
        DrawerContent(
            navController = navController,
            closeDrawer = {},
            mainActivityState = mainActivityStateTest
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignoutBottomSheetContentPreview() {
    val mainActivityStateTest = provideMainActivityStateTest()
    QuizzerAndroidTheme {
        SignoutBottomSheetContent(
            onDismissRequest = { },
            mainActivityState = mainActivityStateTest
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InquiryBottomSheetContentPreview() {
    val mainActivityStateTest = provideMainActivityStateTest()
    QuizzerAndroidTheme {
        InquiryBottomSheetContent(
            onDismissRequest = { },
            mainActivityState = mainActivityStateTest
        )
    }
}