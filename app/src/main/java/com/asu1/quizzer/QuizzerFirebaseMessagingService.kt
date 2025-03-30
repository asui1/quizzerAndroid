package com.asu1.quizzer

import com.asu1.utils.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

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
        sendTokenToServer(token)
        // Send the token to your server or save it for later use.
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    private fun sendTokenToServer(token: String){
        //TODO(서버로 토큰 전송 로직 구현)
    }
}