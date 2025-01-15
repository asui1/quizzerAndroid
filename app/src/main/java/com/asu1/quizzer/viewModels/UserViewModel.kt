package com.asu1.quizzer.viewModels

import ToastManager
import ToastType
import android.net.TrafficStats
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.R
import com.asu1.quizzer.domain.login.LoginUseCase
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.SharedPreferencesManager
import com.asu1.quizzer.util.constants.NetworkTags
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {

    private val _isUserLoggedIn = MutableLiveData(false)
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private val _userData = MutableLiveData<UserDatas?>()
    val userData: MutableLiveData<UserDatas?> get() = _userData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Logger.debug("UserViewModel init")
            val userInfo = SharedPreferencesManager.getUserLoginInfo()
            if (userInfo.email != null) {
                Logger.debug("User login")
                logIn(userInfo.email, userInfo.urlToImage)
            } else{
                Logger.debug("Guest login")
                val guestInfo = SharedPreferencesManager.getGuestLoginInfo()
                if(guestInfo.email != null){
                    guestLogin()
                } else{
                    getGuestAccount()
                }
            }
        }
    }

    private fun getGuestAccount(isKo: Boolean = Locale.getDefault().language == "ko") {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                TrafficStats.setThreadStatsTag(NetworkTags.GET_GUEST_ACCOUNT)
                val response = RetrofitInstance.api.guestAccount(isKo)
                if (response.isSuccessful) {
                    val email = response.body()?.email ?: ""
                    val nickname = response.body()?.nickname ?: ""
                    if (email.isEmpty() || nickname.isEmpty()) {
                        ToastManager.showToast(R.string.can_not_access_server, ToastType.ERROR)
                        return@launch
                    }
                    _userData.postValue(UserDatas(email, nickname, null, emptySet()))
                    SharedPreferencesManager.saveGuestLoginInfo(email, nickname, null, emptySet())
                    _isUserLoggedIn.postValue(false)
                } else {
                    ToastManager.showToast(R.string.can_not_access_server, ToastType.ERROR)
                }

            } catch (e: Exception) {
                ToastManager.showToast(R.string.can_not_access_server, ToastType.ERROR)
            } finally {
                TrafficStats.clearThreadStatsTag()
            }
        }
    }

    private fun guestLogin(
    ){
        Logger.debug("guest login")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val email = SharedPreferencesManager.getGuestLoginInfo().email ?: ""
                if (email.isEmpty()) {
                    getGuestAccount()
                    return@launch
                }
                TrafficStats.setThreadStatsTag(NetworkTags.GUEST_LOGIN)
                val response = loginUseCase.invoke(email)
                val nickname = response.body()?.nickname ?: ""
                if (response.isSuccessful) {
                    _userData.postValue(UserDatas(email, nickname, null, emptySet()))
                    SharedPreferencesManager.saveUserLoginInfo(email, nickname, null, emptySet())
                    _isUserLoggedIn.postValue(false)
                } else {
                    ToastManager.showToast(R.string.can_not_access_server, ToastType.ERROR)
                }

            } catch (e: Exception) {
                ToastManager.showToast(R.string.can_not_access_server, ToastType.ERROR)
            } finally {
                TrafficStats.clearThreadStatsTag()
            }
        }
    }

    fun logIn(email: String, urlToImage: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Logger.debug("login1")
                TrafficStats.setThreadStatsTag(NetworkTags.LOGIN)
                Logger.debug("login3")
                val loginResponse = RetrofitInstance.api.login(email)
                Logger.debug("login2")
                val nickname = loginResponse.body()?.nickname ?: ""
                if (loginResponse.isSuccessful) {
                    Logger.debug("login2")
                    val userTags = loginResponse.body()?.tags ?: emptySet()
                    _userData.postValue(UserDatas(email, nickname, urlToImage, userTags))
                    Logger.debug("login3")
                    SharedPreferencesManager.saveUserLoginInfo(
                        email,
                        nickname,
                        urlToImage,
                        userTags
                    )
                    Logger.debug("login success")
                    ToastManager.showToast(R.string.logged_in, ToastType.SUCCESS)
                    _isUserLoggedIn.postValue(true)
                } else {
                    ToastManager.showToast(R.string.failed_login, ToastType.ERROR)
                }
            } catch (e: Exception) {
                ToastManager.showToast(R.string.failed_login, ToastType.ERROR)
            } finally {
                Logger.debug("login4")
                TrafficStats.clearThreadStatsTag()
            }
        }
    }

    fun signout(email: String){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteUser(email)
                if(response.isSuccessful){
                    ToastManager.showToast(R.string.user_delete_successed, ToastType.SUCCESS)
                    logOut()
                }
                else{
                    ToastManager.showToast(R.string.failed_to_delete_user, ToastType.ERROR)
                }
            }
            catch (e: Exception){
                ToastManager.showToast(R.string.failed_to_delete_user, ToastType.ERROR)
            }
        }
    }

    fun logOut() {
        _userData.value = null
        viewModelScope.launch(Dispatchers.IO) {
            SharedPreferencesManager.clearUserLoginInfo()
        }
        ToastManager.showToast(R.string.logged_out, ToastType.SUCCESS)
        _isUserLoggedIn.postValue(false)
        guestLogin()
    }

    data class UserDatas(
        val email: String?,
        val nickname: String?,
        val urlToImage: String?,
        val tags: Set<String>
    )

}