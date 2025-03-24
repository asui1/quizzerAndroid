package com.asu1.mainpage.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.asu1.customComposable.textField.TagSetter
import com.asu1.customComposable.topBar.RowWithAppIconAndName
import com.asu1.activityNavigation.Route
import com.asu1.quizzer.util.keyboardAsState
import com.asu1.quizzer.viewModels.RegisterViewModel
import com.asu1.quizzer.viewModels.RegisterViewModelActions
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

const val registerSteps = 3

@Composable
fun RegisterScreen(
    navController: NavHostController,
    registerViewModel: RegisterViewModel = hiltViewModel(),
    email: String = "",
    profileUri: String = "",
    login: () -> Unit = {},
){
    val registerStep by registerViewModel.registerStep.observeAsState(0)
    val nickname by registerViewModel.nickname.observeAsState()
    val isError by registerViewModel.isError.observeAsState()
    val tags by registerViewModel.tags.map { it.toPersistentSet()}
        .collectAsStateWithLifecycle(
            persistentSetOf()
        )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { registerSteps-1 },
    )
    val coroutineScope = rememberCoroutineScope()
    val progress by animateFloatAsState(targetValue = registerStep / (registerSteps-1).toFloat(),
        label = "Register Progress"
    )

    LaunchedEffect(Unit){
        registerViewModel.registerViewModelActions(RegisterViewModelActions.IdInit(email, profileUri))
    }

    LaunchedEffect(registerStep) {
        when(registerStep){
            0 -> pagerState.animateScrollToPage(0)
            1 -> pagerState.animateScrollToPage(1)
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
        if(registerStep == 0){
            navController.popBackStack(
                Route.Login,
                inclusive = false
            )
        }else{
            registerViewModel.moveBack()
        }
    }
    Scaffold(
        topBar = { RowWithAppIconAndName() },
    )
    { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Text(
                text = stringResource(R.string.register),
                style = QuizzerTypographyDefaults.quizzerTitleMediumBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LinearProgressIndicator(
                progress = {progress},
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (page) {
                    0 -> UsageAgreement(
                        onProceed = {
                            registerViewModel.agreeTerms()
                        }
                    )

                    1 -> EnterRegisterInputData(
                        nickname = nickname ?: "",
                        registerViewModelAction = {action ->
                            registerViewModel.registerViewModelActions(action)
                        },
                        isError = isError == true,
                        tags = tags,
                        registerStep = registerStep
                    )
                }
            }
        }
    }
}

@Composable
fun UsageAgreement(
    onProceed: () -> Unit = {},
) {
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
                style = QuizzerTypographyDefaults.quizzerHeadlineMediumBold,
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(R.string.privacy_policy_body),
                style = QuizzerTypographyDefaults.quizzerBodySmallNormal,
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
            Text(
                text = stringResource(R.string.agree),
                style = QuizzerTypographyDefaults.quizzerLabelSmallMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsageAgreementPreview() {
    com.asu1.resources.QuizzerAndroidTheme {
        UsageAgreement(
        )
    }
}

@Composable
fun EnterRegisterInputData(
    nickname : String = "",
    registerViewModelAction: (RegisterViewModelActions) -> Unit = {},
    isError : Boolean = false,
    registerStep: Int = 1,
    tags: PersistentSet<String> = persistentSetOf<String>(),
) {
    // Nickname Input UI
    val focusRequester = remember("Nickname") { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var localNickname by remember { mutableStateOf(TextFieldValue(text = nickname)) }
    val isKeyboardOpen by keyboardAsState()
    val stacks = remember { mutableIntStateOf(0) }
    LaunchedEffect(isKeyboardOpen) {
        if(!isKeyboardOpen && stacks.intValue > 0){
            stacks.intValue--
            if(stacks.intValue == 0){
                keyboardController?.hide()
            }
            registerViewModelAction(RegisterViewModelActions.MoveBack)
        }
        else{
            stacks.intValue++
        }
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        ) {
            item("tags") {
                AnimatedVisibility(
                    visible = registerStep > 1,
                ) {
                    TagSetting(
                        tags = tags,
                        toggleTag = {tag ->
                            registerViewModelAction(RegisterViewModelActions.ToggleTag(tag))
                        },
                        register = {
                            registerViewModelAction(RegisterViewModelActions.Register)
                        },
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    )
                }
            }

            item("nickname") {
                TextField(
                    value = localNickname,
                    enabled = registerStep == 1,
                    onValueChange = { textfieldvalue ->
                        if (textfieldvalue.text.length <= 12) {
                            localNickname = textfieldvalue
                            registerViewModelAction(RegisterViewModelActions.UndoError)
                        }
                    },
                    isError = isError,
                    label = {
                        if (isError) Text(
                            text = stringResource(R.string.can_not_use_this_nickname),
                            color = MaterialTheme.colorScheme.error
                        )
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
                            if (localNickname.text.length <= 12)
                                registerViewModelAction(RegisterViewModelActions.SetNickName(localNickname.text))
                        }
                    ),
                    supportingText = {
                        Text(text = buildString {
                            append(stringResource(R.string.length))
                            append("${localNickname.text.length}/12")
                        })
                    }
                )
                Text(
                    text = stringResource(R.string.enter_your_nickname),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
    LaunchedEffect(registerStep){
        if(registerStep == 1){
            focusRequester.requestFocus()
            localNickname = localNickname.copy(selection = TextRange(localNickname.text.length))
            keyboardController?.show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NicknameInputPreview() {
    com.asu1.resources.QuizzerAndroidTheme {
        EnterRegisterInputData(
            nickname = "nickname"
        )
    }
}

@Composable
fun TagSetting(
    modifier: Modifier = Modifier,
    tags: Set<String> = emptySet(),
    toggleTag: (String) -> Unit = {},
    register: () -> Unit = {},
) {
    val tagFocusRequester = remember { FocusRequester() }
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TagSetter(
            tags = tags,
            onClick = {
                toggleTag(it)
            },
            focusRequester = tagFocusRequester,
        )
        Button(
            onClick = {
                register()
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.register),
                style = QuizzerTypographyDefaults.quizzerLabelSmallMedium
            )
        }
    }
    LaunchedEffect(Unit) {
        tagFocusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
fun TagSettingPreview() {
    com.asu1.resources.QuizzerAndroidTheme {
        TagSetting(
            tags = setOf("tag1", "tag2", "tag3")
        )
    }
}