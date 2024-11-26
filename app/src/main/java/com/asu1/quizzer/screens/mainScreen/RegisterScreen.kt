package com.asu1.quizzer.screens.mainScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.RowWithAppIconAndName
import com.asu1.quizzer.composables.TagSetter
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.RegisterViewModel
import kotlinx.coroutines.launch


@Composable
fun RegisterScreen(
    navController: NavHostController,
    registerViewModel: RegisterViewModel = viewModel(),
    email: String = "",
    profileUri: String = "",
    login: () -> Unit = {},
){
    val registerStep by registerViewModel.registerStep.observeAsState()
    val nickname by registerViewModel.nickname.observeAsState()
    val isError by registerViewModel.isError.observeAsState()
    val tags by registerViewModel.tags.observeAsState(emptySet())
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 3 },
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        registerViewModel.setEmail(email)
        registerViewModel.setPhotoUri(profileUri)
    }

    LaunchedEffect(registerStep) {
        when(registerStep){
            0 -> pagerState.animateScrollToPage(0)
            1 -> pagerState.animateScrollToPage(1)
            2 -> pagerState.animateScrollToPage(2)
            3 -> {
                coroutineScope.launch {
                    launch { login() }
                    launch {
                        navController.navigate(Route.Home) {
                            popUpTo(Route.Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }
    BackHandler {
        Logger().debug(
            "Register ${registerStep}"
        )
        if(registerStep == 0){
            navController.popBackStack()
        }else{
            registerViewModel.moveBack()
        }
    }
    Scaffold(
        topBar = {RowWithAppIconAndName()},
    )
    { paddingValues ->
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ){page ->
            when(page){
                0 -> UsageAgreement(
                    onProceed = {
                        registerViewModel.agreeTerms()
                    }
                )
                1 -> NicknameInput(
                    nickname = nickname ?: "",
                    onProceed = {
                        registerViewModel.setNickName(it)
                    },
                    isError = isError ?: false,
                    undoError = {
                        registerViewModel.undoError()
                    }
                )
                2 -> TagSetting(
                    tags = tags,
                    toggleTag = {
                        registerViewModel.toggleTag(it)
                    },
                    register = {
                        registerViewModel.register()
                    }
                )
            }
        }
    }
}

@Composable
fun UsageAgreement(
    onProceed: () -> Unit = {},
) {
    // Usage Agreement UI
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.privacy_policy),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(R.string.privacy_policy_body)
            )
            Spacer(modifier = Modifier.padding(32.dp))
        }
        Button(
            onClick = {
                onProceed()
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = stringResource(R.string.agree))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsageAgreementPreview() {
    QuizzerAndroidTheme {
        UsageAgreement(
        )
    }
}

@Composable
fun NicknameInput(
    nickname : String = "",
    onProceed: (String) -> Unit = {_ -> },
    isError : Boolean = false,
    undoError: () -> Unit = {},
) {
    // Nickname Input UI
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var localNickname by remember { mutableStateOf(TextFieldValue(text = nickname)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.enter_your_nickname),
                style = MaterialTheme.typography.titleMedium,
            )
            TextField(
                value = localNickname,
                onValueChange = {textfieldvalue ->
                    if(textfieldvalue.text.length <= 12) {
                        localNickname = textfieldvalue
                        undoError()
                    }
                },
                isError = isError,
                label = {
                    if(isError) Text(text = stringResource(R.string.duplicate_nickname), color = MaterialTheme.colorScheme.error)
                    else
                        Text(text = stringResource(R.string.nickname))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if(localNickname.text.length <= 12) onProceed(localNickname.text)
                    }
                ),
                supportingText = { Text(text = buildString {
                    append(stringResource(R.string.length))
                    append("${localNickname.text.length}/12")
                }) }
            )
        }
        Button(
            onClick = {
                if(localNickname.text.length <= 12) onProceed(localNickname.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .imePadding()
        ) {
            Text(text = stringResource(R.string.proceed))
        }
    }
    DisposableEffect(Unit) {
        Logger().debug("NicknameInput")
        focusRequester.requestFocus()
        localNickname = localNickname.copy(selection = TextRange(localNickname.text.length))
        keyboardController?.show()
        onDispose { }
    }
}

@Preview(showBackground = true)
@Composable
fun NicknameInputPreview() {
    QuizzerAndroidTheme {
        NicknameInput(
            nickname = "nickname"
        )
    }
}

@Composable
fun TagSetting(
    tags: Set<String> = emptySet(),
    toggleTag: (String) -> Unit = {},
    register: () -> Unit = {},
) {
    // Tag Setting UI
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TagSetter(
            tags = tags,
            onClick = {
                toggleTag(it)
            },
            focusRequester = focusRequester,
        )
        Button(
            onClick = {
                register()
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .imePadding()
        ) {
            Text(text = stringResource(R.string.register))
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Preview(showBackground = true)
@Composable
fun TagSettingPreview() {
    QuizzerAndroidTheme {
        TagSetting(
            tags = setOf("tag1", "tag2", "tag3")
        )
    }
}