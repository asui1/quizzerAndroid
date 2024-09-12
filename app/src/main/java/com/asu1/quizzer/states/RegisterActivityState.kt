package com.asu1.quizzer.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.viewModels.RegisterViewModel

data class RegisterActivityState(
    val agreeTerms : () -> Unit,
    val setNickName: (String) -> Unit,
    val register: (String, String) -> Unit,
    val registerStep: State<Int>,
    val tags: State<List<String>>,
    val removeTag: (String) -> Unit,
    val addTag: (String) -> Unit,
    val moveBack: () -> Unit,
    var email: String?,
    var photoUri: String?,
    val reset: () -> Unit = {},
    )

@Composable
fun rememberRegisterActivityState(
    registerViewmodel: RegisterViewModel = viewModel(),
): RegisterActivityState {
    val registerStep by registerViewmodel.registerStep.observeAsState(initial = 0)
    val tags by registerViewmodel.tags.observeAsState(initial = emptyList())

    return RegisterActivityState(
        agreeTerms = { registerViewmodel.agreeTerms() },
        setNickName = { nickName -> registerViewmodel.setNickName(nickName) },
        register = { email, photoUri -> registerViewmodel.register(email, photoUri) },
        registerStep = rememberUpdatedState(registerStep),
        tags = rememberUpdatedState(tags),
        removeTag = { tag -> registerViewmodel.removeTag(tag) },
        addTag = { tag -> registerViewmodel.addTag(tag) },
        moveBack = { registerViewmodel.moveBack() },
        reset = { registerViewmodel.reset() },
        email = null,
        photoUri = null,
    )
}