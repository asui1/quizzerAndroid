package com.asu1.quizzer.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.UserDataSharedPreferences
import kotlinx.coroutines.launch

class UserViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferencesHelper = UserDataSharedPreferences(application)

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private val _userData = MutableLiveData<UserDatas?>()
    val userData: MutableLiveData<UserDatas?> get() = _userData

    private val _showToast = MutableLiveData<String?>()
    val showToast: LiveData<String?> get() = _showToast

    init {
        val userInfo = sharedPreferencesHelper.getUserLoginInfo()
        if (userInfo.email != null) {
            logIn(userInfo.email, userInfo.urlToImage)
        }
    }


    fun logIn(email: String, urlToImage: String?) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.login(email)
            val nickname = response.body()?.nickname ?: ""
            if(response.isSuccessful){
                val userTags = response.body()?.tags ?: emptySet()
                _userData.value = UserDatas(email, nickname, urlToImage, userTags)
                sharedPreferencesHelper.saveUserLoginInfo(email, nickname, urlToImage, userTags)
                _showToast.postValue("Logged in")
                _isUserLoggedIn.postValue(true)
            }
            else{
                _showToast.postValue("Failed Login")

            }

        }
    }

    fun signout(email: String){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteUser(email)
                if(response.isSuccessful){
                    _showToast.postValue("User Deleted")
                    logOut()
                }
                else{
                    _showToast.postValue("Failed to Delete User")
                }
            }
            catch (e: Exception){
                _showToast.postValue("Failed to Delete User")
            }
        }
    }

    fun logOut() {
        _userData.value = null
        sharedPreferencesHelper.clearUserLoginInfo()
        _showToast.postValue("Logged out")
        _isUserLoggedIn.postValue(false)
    }

    fun toastShown() {
        _showToast.value = null
    }

    fun setToast(message: String) {
        _showToast.value = message
    }

    data class UserDatas(
        val email: String?,
        val nickname: String?,
        val urlToImage: String?,
        val tags: Set<String>
    )

}