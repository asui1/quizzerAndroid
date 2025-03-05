package com.asu1.quizzer.viewModels

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.BuildConfig
import com.asu1.resources.InitializationState
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitializationViewModel() : ViewModel() {

    private val _initializationState = MutableLiveData(InitializationState.CHECKING_FOR_UPDATES)
    val initializationState: LiveData<InitializationState> get() = _initializationState

    private val _isUpdateAvailable = MutableLiveData<Boolean>()
    val isUpdateAvailable: LiveData<Boolean> get() = _isUpdateAvailable

    fun updateInitializationState(step: InitializationState) {
        _initializationState.postValue(step)
    }

    fun noUpdateAvailable(){
        _initializationState.postValue(InitializationState.GETTING_USER_DATA)
        _isUpdateAvailable.postValue(false)
    }
}