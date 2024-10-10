package com.asu1.quizzer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.model.UserRegister
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.Logger
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerStep = MutableLiveData<Int>()
    val registerStep: LiveData<Int> get() = _registerStep

    private val _showToast = MutableLiveData<String?>()
    val showToast: LiveData<String?> get() = _showToast

    private val _nickname = MutableLiveData<String?>()
    val nickname: MutableLiveData<String?> get() = _nickname

    private val _tags = MutableLiveData<Set<String>>(emptySet())
    val tags: LiveData<Set<String>> get() = _tags

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> get() = _email

    private val _photoUri = MutableLiveData<String?>()
    val photoUri: LiveData<String?> get() = _photoUri

    init{
        reset()
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
        Logger().debug("Agree terms")
        _registerStep.postValue(1)
    }

    fun setNickName(nickName: String){
        if(nickName.isEmpty()){
            _showToast.postValue("Please enter a nickname")
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
                    _showToast.postValue("Failed to check nickname")
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
                    _showToast.postValue("Registered successfully")
                    _registerStep.postValue(3)
                }
                else{
                    _showToast.postValue("Failed to register")
                }
            }
            else{
                _showToast.postValue("Failed to register")
            }
        }
    }

    fun addTag(tag: String){
        val tags = _tags.value?.toMutableList()
        if(tags!!.contains(tag)){
            _showToast.postValue("Tag already exists")
            return
        }
        tags.add(tag)
        _tags.postValue(tags!!.toSet())
    }

    fun removeTag(tag: String){
        val tags = _tags.value?.toMutableList()
        if(tags?.remove(tag) == true){
            _tags.postValue(tags!!.toSet())
        }
    }

    fun toastShown() {
        _showToast.value = null
    }
}
