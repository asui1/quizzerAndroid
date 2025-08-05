package com.asu1.activityNavigation

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.asu1.utils.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class QuizzerFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages here.
        Logger.debug(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Logger.debug(TAG, "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Logger.debug(TAG, "Message Notification Body: ${it.title}")
            Logger.debug(TAG, "Message Notification Body: ${it.body}")
        }
    }

    override fun onNewToken(token: String) {
        Logger.debug(TAG, "Refreshed token: $token")
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            FcmServiceEntryPoint::class.java
        )
        val saveFcmTokenUseCase = entryPoint.saveFcmTokenUseCase()
        CoroutineScope(Dispatchers.IO).launch {
            val result =saveFcmTokenUseCase(token)
            if(result.isFailure){
                enqueueFcmRetryWork(token)
            }
        }
    }

    @Suppress("unused")
    private fun enqueueFcmRetryWork(token: String) {
        val inputData = workDataOf("fcm_token" to token)

        val retryRequest = OneTimeWorkRequestBuilder<FcmRetryManager>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(retryRequest)
    }

    companion object {
        private const val TAG = "QuizzerFirebaseMsgService"
    }
}
