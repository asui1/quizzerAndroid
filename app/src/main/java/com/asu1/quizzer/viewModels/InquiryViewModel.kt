package com.asu1.quizzer.viewModels

import ToastManager
import ToastType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.R
import com.asu1.quizzer.model.UserRequest
import com.asu1.quizzer.network.RetrofitInstance
import kotlinx.coroutines.launch

class InquiryViewModel : ViewModel() {
    // Send inquiry to the server
    fun sendInquiry(email: String, subject: String, body: String){
        viewModelScope.launch {
            try {
                val userRequest = UserRequest(email, subject, body)
                val response = RetrofitInstance.api.userRequest(userRequest)
                if(response.isSuccessful){
                    ToastManager.showToast(R.string.inquiry_sent_successfully, ToastType.SUCCESS)
                }
                else{
                    ToastManager.showToast(R.string.failed_to_send_inquiry, ToastType.ERROR)
                }
            }
            catch (e: Exception){
                ToastManager.showToast(R.string.failed_to_send_inquiry, ToastType.ERROR)
            }
        }
    }
}
