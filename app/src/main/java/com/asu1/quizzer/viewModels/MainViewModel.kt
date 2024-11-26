package com.asu1.quizzer.viewModels

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                coroutineScope {
                    val responseDeferred = async { RetrofitInstance.api.getVersion() }
                    val currentVersionDeferred = async {
                        val packageManager = getApplication<Application>().packageManager
                        val packageName = getApplication<Application>().packageName
                        val packageInfo = packageManager.getPackageInfo(packageName, 0)
                        packageInfo.versionName
                    }

                    val response = responseDeferred.await()
                    val currentVersion = currentVersionDeferred.await()

                    if (response.isSuccessful && response.body() != null && response.body()?.latestVersion != null) {
                        if (isUpdateNeeded(response.body()!!.latestVersion, currentVersion)) {
                            _isUpdateAvailable.postValue(true)
                        } else {
                            _isUpdateAvailable.postValue(false)
                        }
                    } else {
                        _isUpdateAvailable.postValue(false)
                    }
                }
            } catch (e: Exception) {
                _isUpdateAvailable.postValue(false)
            }
        }
    }

    fun isUpdateNeeded(latestVersion: String, currentVersion: String): Boolean {
        // compare two versions of form "x.y.z" and "a.b.c" and return true if version1 > version2
        val v1 = latestVersion.split(".").map { it.toInt() }
        val v2 = currentVersion.split(".").map { it.toInt() }
        for(i in 0 until 3) {
            if(v1[i] > v2[i])
                return true
            if(v1[i] < v2[i])
                return false
        }
        return false
    }
}