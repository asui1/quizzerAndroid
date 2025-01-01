package com.asu1.quizzer.util

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object SharedPreferencesManager {
    private const val USER_PREFS = "user_prefs"
    private const val GUEST_PREFS = "guest_prefs"

    private lateinit var userPreferences: SharedPreferences
    private lateinit var guestPreferences: SharedPreferences

    fun init(context: Context) {
        userPreferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        guestPreferences = context.getSharedPreferences(GUEST_PREFS, Context.MODE_PRIVATE)
    }

    fun saveUserLoginInfo(email: String, nickname: String, urlToImage: String?, tags: Set<String>) {
        userPreferences.edit().apply {
            putString("email", email)
            putString("nickname", nickname)
            putString("urlToImage", urlToImage)
            putStringSet("tags", tags)
            putBoolean("agreement", false)
            apply()
        }
    }

    fun getUserLoginInfo(): UserLoginInfo {
        return UserLoginInfo(
            userPreferences.getString("email", null),
            userPreferences.getString("nickname", null),
            userPreferences.getString("urlToImage", null),
            userPreferences.getStringSet("tags", emptySet()),
            userPreferences.getBoolean("agreement", false)
        )
    }

    fun saveGuestLoginInfo(email: String, nickname: String, urlToImage: String?, tags: Set<String>) {
        guestPreferences.edit().apply {
            putString("guestEmail", email)
            putString("guestNickname", nickname)
            putString("guestUri", urlToImage)
            putStringSet("guestTags", tags)
            putBoolean("guestAgreement", false)
            apply()
        }
    }

    fun getGuestLoginInfo(): UserLoginInfo {
        return UserLoginInfo(
            guestPreferences.getString("guestEmail", null),
            guestPreferences.getString("guestNickname", null),
            guestPreferences.getString("guestUri", null),
            guestPreferences.getStringSet("guestTags", emptySet()),
            guestPreferences.getBoolean("guestAgreement", false)
        )
    }

    fun clearUserLoginInfo() {
        userPreferences.edit().clear().apply()
    }

    data class UserLoginInfo(val email: String?, val nickname: String?, val urlToImage: String?, val tags: Set<String>?, val agreement: Boolean)
}