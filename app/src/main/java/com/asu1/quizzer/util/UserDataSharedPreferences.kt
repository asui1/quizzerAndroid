package com.asu1.quizzer.util

import android.content.Context
import android.content.SharedPreferences
import com.asu1.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

//TODO: Migrate to Data store
object SharedPreferencesManager {
    private const val USER_PREFS = "user_prefs"
    private const val GUEST_PREFS = "guest_prefs"

    private lateinit var userPreferences: SharedPreferences
    private lateinit var guestPreferences: SharedPreferences

    suspend fun init(context: Context) {
        coroutineScope {
            val userPrefsDeferred = async(Dispatchers.IO) {
                context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
            }
            val guestPrefsDeferred = async(Dispatchers.IO) {
                context.getSharedPreferences(GUEST_PREFS, Context.MODE_PRIVATE)
            }
            userPreferences = userPrefsDeferred.await()
            guestPreferences = guestPrefsDeferred.await()
            Logger.debug("SharedPreferencesManager initialized")
        }
    }

    suspend fun saveUserLoginInfo(email: String, nickname: String, urlToImage: String?, tags: Set<String>) {
        Logger.debug("Saving user login info")
        withContext(Dispatchers.IO) {
            userPreferences.edit().apply {
                putString("email", email)
                putString("nickname", nickname)
                putString("urlToImage", urlToImage)
                putStringSet("tags", tags)
                putBoolean("agreement", false)
                apply()
            }
        }
    }

    suspend fun getUserLoginInfo(): UserLoginInfo {
        Logger.debug("Getting user login info")
        return withContext(Dispatchers.IO) {
            UserLoginInfo(
                userPreferences.getString("email", null),
                userPreferences.getString("nickname", null),
                userPreferences.getString("urlToImage", null),
                userPreferences.getStringSet("tags", emptySet()),
                userPreferences.getBoolean("agreement", false)
            )
        }
    }

    suspend fun saveGuestLoginInfo(email: String, nickname: String, urlToImage: String?, tags: Set<String>) {
        Logger.debug("Saving guest login info")
        withContext(Dispatchers.IO) {
            guestPreferences.edit().apply {
                putString("guestEmail", email)
                putString("guestNickname", nickname)
                putString("guestUri", urlToImage)
                putStringSet("guestTags", tags)
                putBoolean("guestAgreement", false)
                apply()
            }
        }
    }


    suspend fun getGuestLoginInfo(): UserLoginInfo {
        Logger.debug("Getting guest login info")
        return withContext(Dispatchers.IO) {
            UserLoginInfo(
                guestPreferences.getString("guestEmail", null),
                guestPreferences.getString("guestNickname", null),
                guestPreferences.getString("guestUri", null),
                guestPreferences.getStringSet("guestTags", emptySet()),
                guestPreferences.getBoolean("guestAgreement", false)
            )
        }
    }

    suspend fun clearUserLoginInfo() {
        withContext(Dispatchers.IO) {
            userPreferences.edit().clear().apply()
        }
    }

    data class UserLoginInfo(val email: String?, val nickname: String?, val urlToImage: String?, val tags: Set<String>?, val agreement: Boolean)
}