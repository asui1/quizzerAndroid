package com.asu1.mainpage.viewModels

import SnackBarManager
import ToastType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.network.RetrofitInstance
import com.asu1.resources.R
import com.asu1.userdatamodels.UserActivity
import com.asu1.userdatausecase.GetUserActivitiesUseCase
import com.asu1.userdatausecase.InitLoginUseCase
import com.asu1.userdatausecase.LoginWithEmailUseCase
import com.asu1.userdatausecase.LogoutToGuestUseCase
import com.asu1.utils.LanguageSetter
import com.asu1.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val initLoginUseCase: InitLoginUseCase,
    private val getUserActivitiesUseCase: GetUserActivitiesUseCase,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val logoutToGuestUseCase: LogoutToGuestUseCase,
): ViewModel() {

    private val _isUserLoggedIn = MutableLiveData(false)
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private val _userData = MutableLiveData<UserData?>()
    val userData: MutableLiveData<UserData?> get() = _userData

    private val _userActivities = MutableStateFlow<List<UserActivity>>(emptyList())
    val userActivities: Flow<PersistentList<UserActivity>> =
        _userActivities.map { it.toPersistentList() }

    fun initLogin(onDone: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            val userInfo = try {
                initLoginUseCase(LanguageSetter.isKo)
            } catch (e: IOException) {
                Logger.debug("Network error during init login", e)
                SnackBarManager.showSnackBar(
                    R.string.can_not_access_server,
                    ToastType.ERROR
                )
                return@launch
            } catch (e: HttpException) {
                Logger.debug("Server error during init login: HTTP ${e.code()}", e)
                SnackBarManager.showSnackBar(
                    R.string.can_not_access_server,
                    ToastType.ERROR
                )
                return@launch
            }

            if (userInfo == null) {
                Logger.debug("Init login failed: no user info returned")
                SnackBarManager.showSnackBar(
                    R.string.can_not_access_server,   // create a string like “Unable to log you in”
                    ToastType.ERROR
                )
                return@launch
            }

            _userData.postValue(
                UserData(
                    email      = userInfo.email,
                    nickname   = userInfo.nickname,
                    urlToImage = userInfo.urlToImage,
                    tags       = userInfo.tags?.toPersistentSet() ?: persistentSetOf()
                )
            )

            _isUserLoggedIn.postValue(userInfo.email.contains("@gmail"))
            onDone()
        }
    }


    fun login(email: String, profileUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userInfo = try {
                loginWithEmailUseCase(email, profileUri)
            } catch (e: IOException) {
                Logger.debug("Network error during login", e)
                SnackBarManager.showSnackBar(
                    R.string.can_not_access_server,
                    ToastType.ERROR
                )
                return@launch
            } catch (e: HttpException) {
                Logger.debug("Server error during login: HTTP ${e.code()}", e)
                SnackBarManager.showSnackBar(
                    R.string.can_not_access_server,
                    ToastType.ERROR
                )
                return@launch
            }

            if (userInfo == null) {
                Logger.debug("Login failed: no user info returned")
                SnackBarManager.showSnackBar(
                    R.string.can_not_access_server,  // e.g. “Unable to log you in”
                    ToastType.ERROR
                )
                return@launch
            }

            _isUserLoggedIn.postValue(true)
            _userData.postValue(
                UserData(
                    email      = userInfo.email,
                    nickname   = userInfo.nickname,
                    urlToImage = userInfo.urlToImage,
                    tags       = userInfo.tags?.toPersistentSet() ?: persistentSetOf()
                )
            )
            SnackBarManager.showSnackBar(
                R.string.logged_in,
                ToastType.SUCCESS
            )
        }
    }


    fun getUserActivities(){
        if(_userData.value == null){
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            getUserActivitiesUseCase(_userData.value!!.email ?: "").onSuccess {
                _userActivities.value = it
            }.onFailure {
                Logger.debug("Get user activities failed ${it.message}")
                SnackBarManager.showSnackBar(R.string.can_not_access_server, ToastType.ERROR)
            }
        }
    }

    fun signOut(email: String){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteUser(email)
                if(response.isSuccessful){
                    SnackBarManager.showSnackBar(R.string.user_delete_successed, ToastType.SUCCESS)
                    logOut()
                }
                else{
                    SnackBarManager.showSnackBar(R.string.failed_to_delete_user, ToastType.ERROR)
                }
            } catch (e: IOException) {
                Logger.debug("Network error during sign out", e)
                SnackBarManager.showSnackBar(R.string.failed_to_delete_user, ToastType.ERROR)
            } catch (e: HttpException) {
                Logger.debug("Server error during sign out: HTTP ${e.code()}", e)
                SnackBarManager.showSnackBar(R.string.failed_to_delete_user, ToastType.ERROR)
            }
        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            val userInfo = try {
                logoutToGuestUseCase()
            } catch (e: IOException) {
                Logger.debug("Network error during logout", e)
                SnackBarManager.showSnackBar(
                    R.string.failed_request,
                    ToastType.ERROR
                )
                return@launch
            } catch (e: HttpException) {
                Logger.debug("Server error during logout: HTTP ${e.code()}", e)
                SnackBarManager.showSnackBar(
                    R.string.failed_request,
                    ToastType.ERROR
                )
                return@launch
            }

            if (userInfo == null) {
                Logger.debug("Logout failed: no user info returned")
                SnackBarManager.showSnackBar(
                    R.string.can_not_access_server,  // e.g. “Unable to log you out”
                    ToastType.ERROR
                )
                return@launch
            }

            _isUserLoggedIn.postValue(false)
            _userData.postValue(
                UserData(
                    email      = userInfo.email,
                    nickname   = userInfo.nickname,
                    urlToImage = userInfo.urlToImage,
                    tags       = userInfo.tags?.toPersistentSet() ?: persistentSetOf()
                )
            )
            SnackBarManager.showSnackBar(
                R.string.logged_out,
                ToastType.SUCCESS
            )
        }
    }


    data class UserData(
        val email: String?,
        val nickname: String?,
        val urlToImage: String?,
        val tags: PersistentSet<String>
    )
}

val sampleUserData = UserViewModel.UserData(
    email = "whwkd122@gmail.com",
    nickname = "test",
    urlToImage = null,
    tags = persistentSetOf("Test", "Admin", "Manager")
)
