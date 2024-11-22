package com.asu1.quizzer.viewModels

import ToastManager
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.R
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.UserDataSharedPreferences
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferencesHelper = UserDataSharedPreferences(application)

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private val _userData = MutableLiveData<UserDatas?>()
    val userData: MutableLiveData<UserDatas?> get() = _userData

    init {
        viewModelScope.launch {
            val userInfo = sharedPreferencesHelper.getUserLoginInfo()
            if (userInfo.email != null) {
                logIn(userInfo.email, userInfo.urlToImage)
            }
        }
    }


    fun logIn(email: String, urlToImage: String?) {
        try{

        viewModelScope.launch {
            val response = RetrofitInstance.api.login(email)
            val nickname = response.body()?.nickname ?: ""
            if (response.isSuccessful) {
                val userTags = response.body()?.tags ?: emptySet()
                _userData.value = UserDatas(email, nickname, urlToImage, userTags)
                sharedPreferencesHelper.saveUserLoginInfo(email, nickname, urlToImage, userTags)
                ToastManager.showToast(R.string.logged_in, ToastType.SUCCESS)
                _isUserLoggedIn.postValue(true)
            } else {
                ToastManager.showToast(R.string.failed_login, ToastType.ERROR)
            }
        }

        } catch (e: Exception) {
            ToastManager.showToast(R.string.failed_login, ToastType.ERROR)
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
        sharedPreferencesHelper.clearUserLoginInfo()
        ToastManager.showToast(R.string.logged_out, ToastType.SUCCESS)
        _isUserLoggedIn.postValue(false)
    }

    data class UserDatas(
        val email: String?,
        val nickname: String?,
        val urlToImage: String?,
        val tags: Set<String>
    )

}