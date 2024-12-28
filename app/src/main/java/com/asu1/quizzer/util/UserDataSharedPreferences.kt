package com.asu1.quizzer.util

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class UserDataSharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val guestAccountPreferences: SharedPreferences = context.getSharedPreferences("guest_prefs", Context.MODE_PRIVATE)

    fun saveUserLoginInfo(email: String, nickname: String, urlToImage: String?, tags: Set<String>) {
        sharedPreferences.edit().apply {
            putString("email", email)
            putString("nickname", nickname)
            putString("urlToImage", urlToImage)
            putStringSet("tags", tags)
            putBoolean("agreement", false)
            apply()
        }
    }

    suspend fun getUserLoginInfo(): UserLoginInfo = coroutineScope {
        val emailDeferred = async { sharedPreferences.getString("email", null) }
        val nicknameDeferred = async { sharedPreferences.getString("nickname", null) }
        val urlToImageDeferred = async { sharedPreferences.getString("urlToImage", null) }
        val tagsDeferred = async { sharedPreferences.getStringSet("tags", emptySet()) }
        val agreementDeferred = async { sharedPreferences.getBoolean("agreement", false) }

        UserLoginInfo(
            emailDeferred.await(),
            nicknameDeferred.await(),
            urlToImageDeferred.await(),
            tagsDeferred.await(),
            agreementDeferred.await()
        )
    }

    fun saveGuestLoginInfo(email: String, nickname: String, urlToImage: String?, tags: Set<String>) {
        guestAccountPreferences.edit().apply {
            putString("guestEmail", email)
            putString("guestNickname", nickname)
            putString("guestUri", urlToImage)
            putStringSet("guestTags", tags)
            putBoolean("guestAgreement", false)
            apply()
        }
    }

    suspend fun getGuestLoginInfo(): UserLoginInfo = coroutineScope {
        val emailDeferred = async { guestAccountPreferences.getString("guestEmail", null) }
        val nicknameDeferred = async { guestAccountPreferences.getString("guestNickname", null) }
        val urlToImageDeferred = async { guestAccountPreferences.getString("guestUri", null) }
        val tagsDeferred = async { guestAccountPreferences.getStringSet("guestTags", emptySet()) }
        val agreementDeferred = async { guestAccountPreferences.getBoolean("guestAgreement", false) }

        UserLoginInfo(
            emailDeferred.await(),
            nicknameDeferred.await(),
            urlToImageDeferred.await(),
            tagsDeferred.await(),
            agreementDeferred.await()
        )
    }

    fun clearUserLoginInfo() {
        sharedPreferences.edit().clear().apply()
    }

    data class UserLoginInfo(val email: String?, val nickname: String?, val urlToImage: String?, val tags: Set<String>?, val agreement: Boolean)
}