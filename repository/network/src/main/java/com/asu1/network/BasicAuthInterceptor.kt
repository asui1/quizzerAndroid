package com.asu1.network

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject

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
fun getErrorMessage(jsonResponse: String): String? {
    val jsonObject = JSONObject(jsonResponse)
    return jsonObject.optString("error")
}