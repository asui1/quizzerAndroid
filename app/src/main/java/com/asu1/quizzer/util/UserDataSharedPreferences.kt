package com.asu1.quizzer.util

import android.content.Context
import android.content.SharedPreferences

class UserDataSharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserLoginInfo(email: String, nickname: String, urlToImage: String?, tags: Set<String>) {
        sharedPreferences.edit().apply {
            putString("email", email)
            putString("nickname", nickname)
            putString("urlToImage", urlToImage)
            putStringSet("tags", tags)
            putBoolean("agreement", true)
            apply()
        }
    }

    fun getUserLoginInfo(): UserLoginInfo {
        val email = sharedPreferences.getString("email", null)
        val nickname = sharedPreferences.getString("nickname", null)
        val urlToImage = sharedPreferences.getString("urlToImage", null)
        val tags = sharedPreferences.getStringSet("tags", emptySet())
        val agreement = sharedPreferences.getBoolean("agreement", false)
        return UserLoginInfo(email, nickname, urlToImage, tags, agreement)
    }

    fun clearUserLoginInfo() {
        sharedPreferences.edit().clear().apply()
    }

    data class UserLoginInfo(val email: String?, val nickname: String?, val urlToImage: String?, val tags: Set<String>?, val agreement: Boolean)
}