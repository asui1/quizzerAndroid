package com.asu1.mainpage.viewModels

import SnackBarManager
import ToastType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.appdata.stringFilter.StringFilterRepository
import com.asu1.resources.R
import com.asu1.userdatamodels.UserRegister
import com.asu1.userdatausecase.account.CheckDuplicateNicknameUseCase
import com.asu1.userdatausecase.account.RegisterUserUseCase
import com.asu1.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val stringFilterRepository: StringFilterRepository,
    private val checkDuplicateNickname: CheckDuplicateNicknameUseCase,
    private val registerUser: RegisterUserUseCase,
) : ViewModel() {

    private val _registerStep = MutableLiveData(0)
    val registerStep: LiveData<Int> get() = _registerStep

    private val _nickname = MutableLiveData<String?>(null)
    val nickname: MutableLiveData<String?> get() = _nickname

    private val _tags = MutableStateFlow<Set<String>>(emptySet())
    val tags: Flow<PersistentSet<String>> =
        _tags.map { it.toPersistentSet() }

    private val _email = MutableLiveData<String?>(null)
    val email: LiveData<String?> get() = _email

    private val _photoUri = MutableLiveData<String?>(null)
    val photoUri: LiveData<String?> get() = _photoUri

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> get() = _isError

    fun registerViewModelActions(action: RegisterViewModelActions){
        @Suppress("REDUNDANT_ELSE_IN_WHEN")
        when(action){
            is RegisterViewModelActions.UndoError -> undoError()
            is RegisterViewModelActions.MoveBack -> moveBack()
            is RegisterViewModelActions.AgreeTerms -> agreeTerms()
            is RegisterViewModelActions.Register -> register()
            is RegisterViewModelActions.IdInit -> {
                setId(action.email, action.photoUri)
            }
            is RegisterViewModelActions.SetNickName -> setNickName(action.nickName)
            is RegisterViewModelActions.ToggleTag -> toggleTag(action.tag)
            else -> {}
        }
    }

    fun undoError(){
        if(_isError.value == true) _isError.postValue(false)
    }

    fun setId(email: String, photoUri: String){
        _email.postValue(email)
        _photoUri.postValue(photoUri)
    }

    fun moveBack(){
        if(_registerStep.value == 0) return
        _registerStep.value = _registerStep.value!! - 1
    }

    fun agreeTerms(){
        _registerStep.value = 1
    }

    fun setNickName(nickName: String) {
        if (nickName.isBlank()) {
            SnackBarManager.showSnackBar(R.string.please_enter_a_nickname, ToastType.ERROR)
            _isError.value = true
            return
        }

        viewModelScope.launch {
            // 1) local validations
            val containsAdminWord = stringFilterRepository.containsAdminWord(nickName)
            val containsInappropriateWord = stringFilterRepository.containsInappropriateWord(nickName)

            if (containsAdminWord || containsInappropriateWord) {
                _isError.postValue(true)
                val messageRes =
                    if (containsInappropriateWord) R.string.nickname_contains_inappropriate_word
                    else R.string.nickname_contains_admin_word
                SnackBarManager.showSnackBar(messageRes, ToastType.ERROR)
                return@launch
            }

            // 2) server duplication check via use case
            checkDuplicateNickname(nickName)
                .onSuccess {
                    Logger.debug("Can use this nickname")
                    _nickname.value = nickName
                    _registerStep.postValue(2)
                    _isError.postValue(false)
                }
                .onFailure { e ->
                    Logger.debug("Nickname check failed: ${e.message}")
                    _isError.postValue(true)
                    SnackBarManager.showSnackBar(R.string.can_not_use_this_nickname, ToastType.ERROR)
                }
        }
    }

    fun register() {
        val email = _email.value
        val nickname = _nickname.value
        val tags = _tags.value
        val photo = photoUri.value ?: ""

        if (email.isNullOrBlank() || nickname.isNullOrBlank()) {
            SnackBarManager.showSnackBar(R.string.failed_to_register, ToastType.ERROR)
            return
        }

        viewModelScope.launch {
            val req = UserRegister(
                email = email,
                nickname = nickname,
                tags = tags.toList(),
                idIcon = photo
            )

            registerUser(req)
                .onSuccess {
                    SnackBarManager.showSnackBar(R.string.registered_successfully, ToastType.SUCCESS)
                    _registerStep.postValue(3)
                }
                .onFailure { e ->
                    Logger.debug("register failed: ${e.message}")
                    SnackBarManager.showSnackBar(R.string.failed_to_register, ToastType.ERROR)
                }
        }
    }

    fun toggleTag(tag: String) {
        val current = _tags.value

        // remove path is instant (no suspend)
        if (tag in current) {
            _tags.value = current - tag
            return
        }

        // add path: validate first, then add atomically against latest value
        viewModelScope.launch {
            if (stringFilterRepository.containsInappropriateWord(tag)) {
                _isError.postValue(true)
                SnackBarManager.showSnackBar(R.string.contains_inappropriate_word, ToastType.ERROR)
                return@launch
            }
            _isError.postValue(false)
            _tags.update { it + tag }   // uses latest state; avoids lost updates
        }
    }
}

sealed class RegisterViewModelActions{
    data object UndoError: RegisterViewModelActions()
    data object MoveBack: RegisterViewModelActions()
    data object AgreeTerms: RegisterViewModelActions()
    data object Register: RegisterViewModelActions()
    data class IdInit(val email: String, val photoUri: String): RegisterViewModelActions()
    data class SetNickName(val nickName: String): RegisterViewModelActions()
    data class ToggleTag(val tag: String): RegisterViewModelActions()
}
