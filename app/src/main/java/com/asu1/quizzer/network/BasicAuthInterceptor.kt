package com.asu1.quizzer.network

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor() : Interceptor {
    private val username: String = SecurePreferences.USERNAME
    private val password: String = SecurePreferences.PASSWORD
    private val credentials: String = "Basic " + Base64.encodeToString("$username:$password".toByteArray(), Base64.NO_WRAP)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", credentials)
            .build()
        return chain.proceed(request)
    }
}