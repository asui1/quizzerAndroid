package com.asu1.quizzer.viewModels

import ToastManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.R
import com.asu1.quizzer.model.UserRegister
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.Logger
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerStep = MutableLiveData<Int>(0)
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
        _isError.postValue(false)
    }

    fun reset(){
        _registerStep.postValue(0)
        _tags.postValue(emptySet())
        _nickname.value = null
        _email.value = null
        _photoUri.value = null
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
        _registerStep.postValue(_registerStep.value!! - 1)
    }

    fun agreeTerms(){
        _registerStep.postValue(1)
    }

    fun setNickName(nickName: String){
        if(nickName.isEmpty()){
            ToastManager.showToast(R.string.please_enter_a_nickname, ToastType.ERROR)
        }
        else{
            viewModelScope.launch {
                val response = RetrofitInstance.api.checkDuplicateNickname(nickName)
                if(response.isSuccessful){
                    if(response.code() == 200){
                        _nickname.value = nickName
                        _registerStep.postValue(2)
                    }
                }
                else {
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
