package com.asu1.mainpage.viewModels

import SnackBarManager
import ToastType
import android.net.TrafficStats
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.network.RetrofitInstance
import com.asu1.resources.NetworkTags
import com.asu1.resources.R
import com.asu1.userdatamodels.UserRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.logging.Logger

class InquiryViewModel : ViewModel() {
    // Send inquiry to the server
    fun sendInquiry(email: String, subject: String, body: String){
        viewModelScope.launch {
            try {
                TrafficStats.setThreadStatsTag(NetworkTags.SEND_INQUIRY)
                val userRequest = UserRequest(email, subject, body)
                val response = RetrofitInstance.api.userRequest(userRequest)
                if(response.isSuccessful){
                    SnackBarManager.showSnackBar(R.string.inquiry_sent_successfully, ToastType.SUCCESS)
                }
                else{
                    SnackBarManager.showSnackBar(R.string.failed_to_send_inquiry, ToastType.ERROR)
                }
            } catch (e: IOException) {
                SnackBarManager.showSnackBar(
                    R.string.failed_to_send_inquiry,
                    ToastType.ERROR
                )
                Logger.getLogger("sendInquiry").info("sendInquiry ioException $e")
            } catch (e: HttpException) {
                SnackBarManager.showSnackBar(
                    R.string.failed_to_send_inquiry,
                    ToastType.ERROR
                )
                Logger.getLogger("sendInquiry").info("sendInquiry HttpException $e")
            } finally {
                TrafficStats.clearThreadStatsTag()
            }
        }
    }
}
