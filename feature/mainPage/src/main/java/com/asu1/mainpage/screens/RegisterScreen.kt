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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.textField.TagSetter
import com.asu1.customComposable.topBar.RowWithAppIconAndName
import com.asu1.customComposable.uiUtil.keyboardAsState
import com.asu1.mainpage.viewModels.RegisterViewModel
import com.asu1.mainpage.viewModels.RegisterViewModelActions
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.utils.LanguageSetter
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.launch

const val REGISTER_STEPS = 3

@Composable
fun RegisterScreen(
    navController: NavHostController,
    email: String,
    profileUri: String
) {
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val userViewModel: UserViewModel = viewModel()

    val state = rememberRegisterState(
        registerViewModel = registerViewModel,
        email = email,
        profileUri = profileUri
    )
    val onEvent: (RegisterViewModelActions) -> Unit = { registerViewModel.registerViewModelActions(it) }
    val isError = registerViewModel.isError.observeAsState()

    RegisterScreenContent(
        state = state,
        onNavigateBack = { navController.popBackStack(Route.Login, false) },
        onNavigateHome = { navController.navigate(Route.Home) { popUpTo(Route.Home) { inclusive = false } } },
        onLogin = { userViewModel.login(email, profileUri) },
        onEvent = onEvent,
        isError = isError.value ?: false,
    )
}

@Stable
data class RegisterState(
    val step: Int,
    val nickname: String?,
    val tags: Set<String>
)

@Composable
private fun rememberRegisterState(
    registerViewModel: RegisterViewModel,
    email: String,
    profileUri: String
): RegisterState {
    val step by registerViewModel.registerStep.observeAsState(0)
    val nickname by registerViewModel.nickname.observeAsState()
    val tags by registerViewModel.tags.collectAsStateWithLifecycle(persistentSetOf())

    // init once
    LaunchedEffect(email, profileUri) {
        registerViewModel.registerViewModelActions(RegisterViewModelActions.IdInit(email, profileUri))
    }

    return remember(step, nickname, tags) {
        RegisterState(step = step, nickname = nickname, tags = tags)
    }
}

@Composable
private fun RegisterScreenContent(
    state: RegisterState,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit,
    onLogin: () -> Unit,
    onEvent: (RegisterViewModelActions) -> Unit,
    isError: Boolean,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { REGISTER_STEPS - 1 })
    val scope = rememberCoroutineScope()
    val progress by animateFloatAsState(
        targetValue = state.step / (REGISTER_STEPS - 1).toFloat(),
        label = "Register Progress"
    )

    BackHandler {
        if (state.step == 0) onNavigateBack()
        else scope.launch { onEvent(RegisterViewModelActions.MoveBack) }
    }

    // step changes pager
    LaunchedEffect(state.step) {
        if (state.step < REGISTER_STEPS - 1) {
            pagerState.animateScrollToPage(state.step)
        } else {
            onLogin()
            onNavigateHome()
        }
    }

    Scaffold(topBar = { RowWithAppIconAndName() }) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            RegisterHeader(progress)
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> UsageAgreement(onProceed = { onEvent(RegisterViewModelActions.AgreeTerms) })
                    1 -> EnterRegisterInputData(
                        state = state,
                        isError = isError,
                        onAction = onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterHeader(progress: Float) {
    Text(
        text = stringResource(R.string.register),
        style = QuizzerTypographyDefaults.quizzerTitleMediumBold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth(),
        color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
}

@Composable
fun UsageAgreement(
    onProceed: () -> Unit = {},
) {
    val annotatedString = remember(LanguageSetter.lang) {
        when (LanguageSetter.lang) {
            "ko" -> annotatedPolicyKo()
            else -> annotatedPolicyEn()
        }
    }

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
                text = annotatedString,
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
    state: RegisterState,
    isError: Boolean,
    onAction: (RegisterViewModelActions) -> Unit
) {
    // Handle keyboard hide/back logic
    val isKeyboardOpen by keyboardAsState()
    var stackCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(isKeyboardOpen) {
        if (isKeyboardOpen) {
            stackCount++
        } else if (stackCount > 0) {
            stackCount--
            onAction(RegisterViewModelActions.MoveBack)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        AnimatedVisibility(visible = state.step > 1) {
            TagSettingSection(
                tags = state.tags,
                onToggleTag = { tag -> onAction(RegisterViewModelActions.ToggleTag(tag)) },
                onRegister = { onAction(RegisterViewModelActions.Register) }
            )
        }

        NicknameInputSection(
            nickname = state.nickname.orEmpty(),
            isError = isError,
            onNicknameChange = { text ->
                onAction(RegisterViewModelActions.UndoError)
                onAction(RegisterViewModelActions.SetNickName(text))
            },
            onProceed = {
                onAction(RegisterViewModelActions.SetNickName(state.nickname.orEmpty()))
            }
        )
    }
}

@Composable
private fun TagSettingSection(
    tags: Set<String>,
    onToggleTag: (String) -> Unit,
    onRegister: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        TagSetter(
            tags = tags,
            onClick = onToggleTag
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRegister, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.register))
        }
    }
}

@Composable
private fun NicknameInputSection(
    nickname: String,
    isError: Boolean,
    onNicknameChange: (String) -> Unit,
    onProceed: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        TextField(
            value = nickname,
            onValueChange = onNicknameChange,
            isError = isError,
            label = { Text(stringResource(R.string.nickname)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                onProceed()
                keyboardController?.hide()
            })
        )
        if (isError) {
            Text(
                text = stringResource(R.string.can_not_use_this_nickname),
                color = MaterialTheme.colorScheme.error,
                style = QuizzerTypographyDefaults.quizzerLabelSmallMedium
            )
        }
    }
    // Auto-focus on first step
    LaunchedEffect(nickname) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Preview(showBackground = true)
@Composable
fun NicknameInputPreview() {
    val state = rememberRegisterState(
        registerViewModel = hiltViewModel(),
        email = "Test@Test.com",
        profileUri = "ThisIsTestUri",
    )
    com.asu1.resources.QuizzerAndroidTheme {
        EnterRegisterInputData(
            state = state,
            isError = false,
            onAction = {},
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
        Spacer(modifier = Modifier.height(16.dp))
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
