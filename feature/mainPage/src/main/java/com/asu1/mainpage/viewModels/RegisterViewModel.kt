package com.asu1.mainpage.viewModels

import SnackBarManager
import ToastType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.appdata.stringFilter.StringFilterRepository
import com.asu1.resources.R
import com.asu1.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val stringFilterRepository: StringFilterRepository,
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

    private val _isError = MutableLiveData<Boolean>(false)
    val isError: LiveData<Boolean> get() = _isError

    fun registerViewModelActions(action: RegisterViewModelActions){
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

    fun setNickName(nickName: String){
        if(nickName.isEmpty()){
            SnackBarManager.showSnackBar(R.string.please_enter_a_nickname, ToastType.ERROR)
            _isError.value = true
            return
        }

        viewModelScope.launch {
            val containsAdminWord = stringFilterRepository.containsAdminWord(nickName)
            val containsInappropriateWord = stringFilterRepository.containsInappropriateWord(nickName)
            if (containsAdminWord || containsInappropriateWord) {
                _isError.postValue(true)

                // 🚨 Show appropriate toast message
                val messageRes = when {
                    containsInappropriateWord -> R.string.nickname_contains_inappropriate_word
                    containsAdminWord -> R.string.nickname_contains_admin_word
                    else -> R.string.can_not_use_this_nickname // Fallback (should not happen)
                }

                SnackBarManager.showSnackBar(messageRes, ToastType.ERROR)
                return@launch
            }

            val response = com.asu1.network.RetrofitInstance.api.checkDuplicateNickname(nickName)
            if(response.isSuccessful){
                if(response.code() == 200){
                    Logger.debug("Can use this nickname")
                    _nickname.value = nickName
                    _registerStep.postValue(2)
                }
            }
            else {
                _isError.postValue(true)
                SnackBarManager.showSnackBar(R.string.can_not_use_this_nickname, ToastType.ERROR)
            }
        }
    }

    fun register(){
        if(_email.value == null){
            return
        }
        viewModelScope.launch {
            val response = com.asu1.network.RetrofitInstance.api.register(
                com.asu1.userdatamodels.UserRegister(
                    _email.value!!,
                    _nickname.value!!,
                    _tags.value!!.toList(),
                    photoUri.value ?: ""
                )
            )
            if(response.isSuccessful){
                if(response.code() == 201){
                    SnackBarManager.showSnackBar(R.string.registered_successfully, ToastType.SUCCESS)
                    _registerStep.postValue(3)
                }
                else{
                    SnackBarManager.showSnackBar(R.string.failed_to_register, ToastType.ERROR)
                }
            }
            else{
                SnackBarManager.showSnackBar(R.string.failed_to_register, ToastType.ERROR)
            }
        }
    }
    fun toggleTag(tag: String){
        val tags = _tags.value?.toMutableList() ?: mutableListOf()
        if(tags.contains(tag)){
            tags.remove(tag)
            _tags.value = tags.toSet()
        }
        else{
            viewModelScope.launch {
                val containsInappropriateWord = stringFilterRepository.containsInappropriateWord(tag)
                if (containsInappropriateWord) {
                    _isError.postValue(true)
                    SnackBarManager.showSnackBar(R.string.contains_inappropriate_word, ToastType.ERROR)
                    return@launch
                }
                tags.add(tag)
                _tags.value = tags.toSet()
            }
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