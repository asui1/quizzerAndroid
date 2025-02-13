package com.asu1.quizzer.viewModels

import ToastManager
import ToastType
import android.net.TrafficStats
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.network.RetrofitInstance
import com.asu1.resources.NetworkTags
import com.asu1.resources.R
import com.asu1.userdatausecase.InitLoginUseCase
import com.asu1.userdatausecase.TryLoginUseCase
import com.asu1.utils.LanguageSetter
import com.asu1.utils.Logger
import com.google.common.collect.ImmutableSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val tryLoginUseCase: TryLoginUseCase,
    private val initLoginUseCase: InitLoginUseCase
): ViewModel() {

    private val _isUserLoggedIn = MutableLiveData(false)
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private val _userData = MutableLiveData<UserDatas?>()
    val userData: MutableLiveData<UserDatas?> get() = _userData

    fun initLogin(onDone: () -> Unit = {}){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val userInfo = initLoginUseCase(LanguageSetter.isKo)
                if(userInfo != null){
                    _userData.postValue(UserDatas(userInfo.email, userInfo.nickname, userInfo.urlToImage, ImmutableSet.copyOf(userInfo.tags ?: emptySet())))
                    if(userInfo.email.contains("@gmail")){
                        _isUserLoggedIn.postValue(true)
                    }
                    onDone()
                }
                else{
                    throw Exception("Init login failed")
                }
            }
            catch (e: Exception){
                Logger.debug("Init login failed ${e.message}")
                ToastManager.showToast(R.string.can_not_access_server, ToastType.ERROR)
            }
        }
    }

    fun login(email: String){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val userInfo = tryLoginUseCase()
                if(userInfo != null){
                    _isUserLoggedIn.postValue(true)
                }
                else{
                    throw Exception("Login failed")
                }
            }
            catch (e: Exception){
                Logger.debug("Login failed ${e.message}")
                ToastManager.showToast(R.string.can_not_access_server, ToastType.ERROR)
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
                    _userData.postValue(UserDatas(email, nickname, null, ImmutableSet.of()))
                    //TODO: DATASTORE
                    _isUserLoggedIn.postValue(false)
                } else {
                    ToastManager.showToast(R.string.can_not_access_server, ToastType.ERROR)
                }

            } catch (e: Exception) {
                Logger.debug("Get guest account failed ${e.message}")
                ToastManager.showToast(R.string.can_not_access_server, ToastType.ERROR)
            } finally {
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
        _isUserLoggedIn.postValue(false)
        viewModelScope.launch(Dispatchers.IO) {

        }
        ToastManager.showToast(R.string.logged_out, ToastType.SUCCESS)
    }

    data class UserDatas(
        val email: String?,
        val nickname: String?,
        val urlToImage: String?,
        val tags: ImmutableSet<String>
    )

}