package com.asu1.quizzer.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.RowWithAppIconAndName
import com.asu1.quizzer.composables.TagSetter
import com.asu1.quizzer.states.RegisterActivityState
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.Route

@Composable
fun UsageAgreement(navController: NavHostController, registerActivityState: RegisterActivityState) {
    // Usage Agreement UI
    val context = LocalContext.current
    val registerStep by registerActivityState.registerStep

    LaunchedEffect(registerStep) {
        if (registerStep == 1) {
            navController.navigate(Route.RegisterNickname)
        }
    }
    BackHandler {
        registerActivityState.email = null
        registerActivityState.photoUri = null
        registerActivityState.reset()
        navController.popBackStack()
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                RowWithAppIconAndName()
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
                    registerActivityState.agreeTerms()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text(text = "Agree")
            }
        }
    }
}

@Composable
fun getRegisterActivityState(): RegisterActivityState {

    return RegisterActivityState(
        agreeTerms = {},
        setNickName = {},
        register = { _, _ -> },
        registerStep = remember { mutableIntStateOf(0)},
        tags = remember { mutableStateOf(listOf("Test1", "Test2", "Test3")) },
        removeTag = {},
        addTag = {},
        email = "whwkd122@gmail.com",
        photoUri = null,
        moveBack = {}
    )
}

@Preview
@Composable
fun UsageAgreementPreview() {
    val navController = rememberNavController()


    QuizzerAndroidTheme {
        UsageAgreement(
            navController = navController,
            registerActivityState = getRegisterActivityState()
        )
    }
}

@Composable
fun NicknameInput(navController: NavHostController, registerActivityState: RegisterActivityState) {
    // Nickname Input UI
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var nickname by remember { mutableStateOf("") }
    val registerStep by registerActivityState.registerStep

    LaunchedEffect(registerStep) {
        Logger().printBackStack(navController)
        if (registerStep == 2) {
            navController.navigate(Route.RegisterTags)
        }
    }
    BackHandler {
        registerActivityState.moveBack()
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            RowWithAppIconAndName()
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Enter your nickname",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    TextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                registerActivityState.setNickName(nickname)
                            }
                        )
                    )
                }
                Button(
                    onClick = { registerActivityState.setNickName(nickname) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .imePadding()
                ) {
                    Text(text = "Proceed")
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Preview
@Composable
fun NicknameInputPreview() {
    val navController = rememberNavController()
    QuizzerAndroidTheme {
        NicknameInput(
            navController = navController,
            registerActivityState = getRegisterActivityState()
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSetting(navController: NavHostController, registerActivityState: RegisterActivityState) {
    // Tag Setting UI
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val tags by registerActivityState.tags
    val registerStep by registerActivityState.registerStep


    LaunchedEffect(registerStep) {
        if (registerStep == 3) {
            registerActivityState.email = null
            registerActivityState.photoUri = null
            registerActivityState.reset()
            navController.popBackStack(Route.Login, inclusive = false)
        }
    }

    BackHandler {
        registerActivityState.moveBack()
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            RowWithAppIconAndName()
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                TagSetter(
                    tags = tags,
                    onClickAdd = {it -> registerActivityState.addTag(it) },
                    onClickRemove = {it -> registerActivityState.removeTag(it) },
                    focusRequester = focusRequester,
                )
                Button(
                    onClick = {
                        if (registerActivityState.email != null && registerActivityState.photoUri != null) {
                            registerActivityState.register(
                                registerActivityState.email!!,
                                registerActivityState.photoUri!!
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .imePadding()
                ) {
                    Text(text = "Register")
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Preview
@Composable
fun TagSettingPreview() {
    QuizzerAndroidTheme {
        TagSetting(
            navController = rememberNavController(),
            registerActivityState = getRegisterActivityState()
        )
    }
}