package com.asu1.splashpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asu1.resources.InitializationState

class InitializationViewModel() : ViewModel() {

    private val _initializationState = MutableLiveData(InitializationState.CHECKING_FOR_UPDATES)
    val initializationState: LiveData<InitializationState> get() = _initializationState

    fun updateInitializationState(step: InitializationState) {
        _initializationState.postValue(step)
    }

    fun noUpdateAvailable(){
        _initializationState.postValue(InitializationState.GETTING_USER_DATA)
    }
}