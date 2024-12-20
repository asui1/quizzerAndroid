package com.asu1.quizzer.viewModels

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asu1.quizzer.BuildConfig
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _isUpdateAvailable = MutableLiveData<Boolean>()
    val isUpdateAvailable: LiveData<Boolean> get() = _isUpdateAvailable

    init {
        checkForUpdates()
    }

    fun finishApp() {
        val activity = getApplication<Application>().applicationContext as? Activity
        activity?.finish()
    }

    private fun checkForUpdates() {
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
                _isUpdateAvailable.postValue(false)
            }
        }

        //NEEDED FOR LOCAL TESTING. MUST REMOVE ON RELEASE
        if(BuildConfig.isDebug){
            _isUpdateAvailable.postValue(false)
        }
//        else{
//            viewModelScope.launch {
//                delay(5000)
//                _isUpdateAvailable.postValue(false)
//            }
//        }
    }
}