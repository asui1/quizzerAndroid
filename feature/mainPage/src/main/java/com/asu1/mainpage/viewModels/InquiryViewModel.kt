package com.asu1.mainpage.viewModels

import SnackBarManager
import ToastType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.resources.R
import com.asu1.userdatausecase.SendInquiryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class InquiryViewModel @Inject constructor(
    private val sendInquiryUseCase: SendInquiryUseCase
) : ViewModel() {
    // Send inquiry to the server
    fun sendInquiry(email: String, subject: String, body: String) {
        viewModelScope.launch {
            sendInquiryUseCase(email, subject, body)
                .onSuccess {
                    SnackBarManager.showSnackBar(
                        R.string.inquiry_sent_successfully,
                        ToastType.SUCCESS
                    )
                }
                .onFailure { e ->
                    Logger.getLogger("sendInquiry").info("sendInquiry error: ${e.message}")
                    SnackBarManager.showSnackBar(
                        R.string.failed_to_send_inquiry,
                        ToastType.ERROR
                    )
                }
        }
    }
}
