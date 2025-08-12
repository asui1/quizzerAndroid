package com.asu1.mainpage.viewModels

import SnackBarManager
import ToastType
import android.net.TrafficStats
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.network.AuthApi
import com.asu1.network.runApi
import com.asu1.resources.NetworkTags
import com.asu1.resources.R
import com.asu1.userdatamodels.UserRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class InquiryViewModel @Inject constructor(
    private val authApi: AuthApi,
) : ViewModel() {
    // Send inquiry to the server
    fun sendInquiry(email: String, subject: String, body: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                TrafficStats.setThreadStatsTag(NetworkTags.SEND_INQUIRY)

                val req = UserRequest(email = email, subject = subject, body = body)

                runApi { authApi.userRequest(req) }      // Result<Response<Void>>
                    .mapCatching { resp ->
                        if (!resp.isSuccessful) throw HttpException(resp)
                    }
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
            } finally {
                TrafficStats.clearThreadStatsTag()
            }
        }
    }
}
