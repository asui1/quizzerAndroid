package com.asu1.quizzer.viewModels

import ToastManager
import ToastType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.R
import com.asu1.quizzer.model.UserRegister
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.utils.Logger
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerStep = MutableLiveData(0)
    val registerStep: LiveData<Int> get() = _registerStep

    private val _nickname = MutableLiveData<String?>(null)
    val nickname: MutableLiveData<String?> get() = _nickname

    private val _tags = MutableLiveData<Set<String>>(emptySet())
    val tags: LiveData<Set<String>> get() = _tags

    private val _email = MutableLiveData<String?>(null)
    val email: LiveData<String?> get() = _email

    private val _photoUri = MutableLiveData<String?>(null)
    val photoUri: LiveData<String?> get() = _photoUri

    private val _isError = MutableLiveData<Boolean>(false)
    val isError: LiveData<Boolean> get() = _isError

    fun undoError(){
        if(_isError.value == true) _isError.postValue(false)
    }

    // Proceed 0 : 약관 동의
    // Proceed 1 : 닉네임 입력
    // Proceed 2 : 태그 설정

    fun setEmail(email: String){
        _email.postValue(email)
    }

    fun setPhotoUri(photoUri: String){
        _photoUri.postValue(photoUri)
    }

    fun moveBack(){
        if(_registerStep.value == 0) return
        _registerStep.value = _registerStep.value!! - 1
    }

    fun agreeTerms(){
        Logger.debug("Agree terms")
        _registerStep.value = 1
    }

    fun setNickName(nickName: String){
        Logger.debug("Set nickname")
        if(nickName.isEmpty()){
            ToastManager.showToast(R.string.please_enter_a_nickname, ToastType.ERROR)
            _isError.value = true
        }
        else{
            viewModelScope.launch {
                val response = RetrofitInstance.api.checkDuplicateNickname(nickName)
                if(response.isSuccessful){
                    if(response.code() == 200){
                        Logger.debug("Can use this nickname")
                        _nickname.value = nickName
                        _registerStep.postValue(2)
                    }
                }
                else {
                    _isError.postValue(true)
                    ToastManager.showToast(R.string.can_not_use_this_nickname, ToastType.ERROR)
                }
            }
        }
    }

    fun register(){
        if(_email.value == null){
            return
        }
        viewModelScope.launch {
            val response = RetrofitInstance.api.register(UserRegister(_email.value!!, _nickname.value!!, _tags.value!!.toList(), photoUri.value ?: ""))
            if(response.isSuccessful){
                if(response.code() == 201){
                    ToastManager.showToast(R.string.registered_successfully, ToastType.SUCCESS)
                    _registerStep.postValue(3)
                }
                else{
                    ToastManager.showToast(R.string.failed_to_register, ToastType.ERROR)
                }
            }
            else{
                ToastManager.showToast(R.string.failed_to_register, ToastType.ERROR)
            }
        }
    }
    fun toggleTag(tag: String){
        val tags = _tags.value?.toMutableList() ?: mutableListOf()
        if(tags.contains(tag)){
            tags.remove(tag)
        }
        else{
            tags.add(tag)
        }
        _tags.postValue(tags.toSet())
    }
}
