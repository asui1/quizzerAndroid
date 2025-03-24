package com.asu1.mainpage.viewModels

import SnackBarManager
import ToastType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asu1.appdatamodels.Notification
import com.asu1.appdatausecase.GetNotificationDetailUseCase
import com.asu1.appdatausecase.GetNotificationPageNumberUseCase
import com.asu1.appdatausecase.GetNotificationUseCase
import com.asu1.resources.R
import com.asu1.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val getNotificationDetailUseCase: GetNotificationDetailUseCase,
    private val getNotificationPageNumberUseCase: GetNotificationPageNumberUseCase,
): ViewModel() {

    private val _notificationPages = MutableLiveData(1)
    val notificationPages: LiveData<Int> get() = _notificationPages

    private val _notificationList = MutableStateFlow<List<Notification>>(emptyList())
    val notificationList: StateFlow<List<Notification>> get() = _notificationList.asStateFlow()

    private val _currentPage = MutableLiveData(1)
    val currentPage: LiveData<Int> get() = _currentPage

    private val _currentNotificationDetail = MutableLiveData<Notification?>(null)
    val currentNotificationDetail: LiveData<Notification?> get() = _currentNotificationDetail

    init{
        getNotificationPageNumber()
        getNotificationList(1)
    }

    private fun getNotificationList(page: Int){
        if(page < 1 || page > (_notificationPages.value ?: 1)){
            return
        }

        _currentPage.value = page
        _notificationList.value = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            getNotificationUseCase(page).onSuccess {
                _notificationList.value = it
            }.onFailure {
                SnackBarManager.showSnackBar(R.string.can_not_access_server, ToastType.ERROR)
            }
        }
    }

    private fun getNotificationDetail(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            getNotificationDetailUseCase(id).onSuccess {
                val notification = _notificationList.value.first {notification ->
                    notification.id == id
                }.copy(body = it)
                _currentNotificationDetail.postValue(notification)
            }.onFailure {
                Logger.debug("Error in GetNotificationDetailUseCase: $it")
                SnackBarManager.showSnackBar(R.string.can_not_access_server, ToastType.ERROR)
            }
        }
    }

    private fun getNotificationPageNumber(){
        viewModelScope.launch(Dispatchers.IO) {
            getNotificationPageNumberUseCase().onSuccess {
                _notificationPages.postValue(it)
            }.onFailure {
                SnackBarManager.showSnackBar(R.string.can_not_access_server, ToastType.ERROR)
            }
        }
    }

    private fun removeCurrentNotificationDetail(){
        _currentNotificationDetail.value = null
    }

    fun updateNotificationViewModel(event: NotificationViewModelEvent){
        when(event){
            is NotificationViewModelEvent.GetNotificationDetail -> {
                getNotificationDetail(event.notificationId)
            }
            is NotificationViewModelEvent.RemoveNotificationDetail -> {
                removeCurrentNotificationDetail()
            }
            is NotificationViewModelEvent.GetNotificationList -> {
                getNotificationList(event.page)
            }
        }
    }
}


sealed class NotificationViewModelEvent{
    data class GetNotificationDetail(val notificationId: Int): NotificationViewModelEvent()
    data class GetNotificationList(val page: Int): NotificationViewModelEvent()
    data object RemoveNotificationDetail: NotificationViewModelEvent()
}