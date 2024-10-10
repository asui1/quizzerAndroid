package com.asu1.quizzer.viewModels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.Logger
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _isInternetAvailable = MutableLiveData<Boolean>()
    val isInternetAvailable: LiveData<Boolean> get() = _isInternetAvailable

    private val _isUpdateAvailable = MutableLiveData<Boolean>()
    val isUpdateAvailable: LiveData<Boolean> get() = _isUpdateAvailable

    init {
        checkInternetConnection()
    }

    fun updateInternetConnection() {
        checkInternetConnection()
    }

    fun updateIsUpdateAvailable() {
        checkForUpdates()
    }

    fun finishApp() {
        val activity = getApplication<Application>().applicationContext as? Activity
        activity?.finish()
    }

    private fun checkInternetConnection() {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val activeNetwork = connectivityManager.getNetworkCapabilities(network)
        Logger().debug("Internet Check run")
        _isInternetAvailable.postValue(
            activeNetwork != null && (
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    )
        )
    }

    private fun checkForUpdates() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getVersion()
                val packageManager = getApplication<Application>().packageManager
                val packageName = getApplication<Application>().packageName
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                val currentVersion = packageInfo.versionName
                Logger().debug("Current version: $currentVersion")
                Logger().debug("Latest version: ${response.body()?.latestVersion}")
                if(response.isSuccessful && response.body() != null && response.body()?.latestVersion != null){
                    if(isUpdateNeeded(response.body()!!.latestVersion, currentVersion)){
                        Logger().debug("Update is available")
                        _isUpdateAvailable.postValue(true)
                    }
                    else
                        _isUpdateAvailable.postValue(false)
                }
                else
                    _isUpdateAvailable.postValue(false)
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