package com.asu1.quizzer.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.model.UserRequest
import com.asu1.quizzer.network.RetrofitInstance
import kotlinx.coroutines.launch

class InquiryViewModel : ViewModel() {
    private val _isDone = MutableLiveData<Boolean>()
    val isDone: LiveData<Boolean> get() = _isDone

    private val _showToast = MutableLiveData<String?>()
    val showToast: LiveData<String?> get() = _showToast

    init{
        _isDone.value = false
    }

    fun sendInquiry(email: String, subject: String, body: String){
        viewModelScope.launch {
            try {
                val userRequest = UserRequest(email, subject, body)
                val response = RetrofitInstance.api.userRequest(userRequest)
                if(response.isSuccessful){
                    _isDone.postValue(true)
                }
                else{
                    _showToast.postValue("Failed to send inquiry")
                }
            }
            catch (e: Exception){
                _isDone.value = false
            }
            // Send inquiry to the server
        }
    }

    fun toastShown() {
        _showToast.value = null
    }
}
