package com.asu1.quizzer.viewModels

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.BuildConfig
import com.asu1.resources.InitializationState
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitializationViewModel(application: Application) : AndroidViewModel(application) {

    private val _initializationState = MutableLiveData(InitializationState.CHECKING_FOR_UPDATES)
    val initializationState: LiveData<InitializationState> get() = _initializationState

    private val _isUpdateAvailable = MutableLiveData<Boolean>()
    val isUpdateAvailable: LiveData<Boolean> get() = _isUpdateAvailable

    init {
        checkForUpdates()
    }

    fun finishApp() {
        val activity = getApplication<Application>().applicationContext as? Activity
        activity?.finish()
    }

    fun updateInitializationState(step: InitializationState) {
        _initializationState.postValue(step)
    }

    private fun checkForUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            val appUpdateManager = AppUpdateManagerFactory.create(context)
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    _isUpdateAvailable.postValue(true)
                    // Request the update.
                }else{
                    _initializationState.postValue(InitializationState.GETTING_USER_DATA)
                    _isUpdateAvailable.postValue(false)
                }
            }

        }

        //NEEDED FOR LOCAL TESTING. MUST REMOVE ON RELEASE
        if(BuildConfig.isDebug){
            viewModelScope.launch {
                delay(2000)
                _initializationState.postValue(InitializationState.GETTING_USER_DATA)
                _isUpdateAvailable.postValue(false)
            }
        }
    }
}