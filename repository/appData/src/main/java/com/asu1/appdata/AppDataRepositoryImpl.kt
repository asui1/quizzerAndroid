package com.asu1.appdata

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.asu1.appdatamodels.Notification
import com.asu1.network.RetrofitInstance
import com.asu1.utils.LanguageSetter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val retrofitInstance: RetrofitInstance,
) : AppDataRepository {
    private val Context.dataStore by preferencesDataStore(name = "onBoardingNotification_state")
    private val dataStore = context.dataStore
    private val notificationsCache = mutableMapOf<Int, List<Notification>>()
    private val notificationsBodyCache = mutableMapOf<Int, String>()
    private val onBoardingNotificationIdKey = stringPreferencesKey("onBoardingNotificationId")
    private var notificationPageNumberCache = 0

    override suspend fun getNotification(page: Int): Result<List<Notification>> {
        return try {
            // Check cache first
            val cachedNotifications = notificationsCache[page]

            if (cachedNotifications != null) {
                return Result.success(cachedNotifications)
            }

            // Make your API call
            val response = retrofitInstance.api.getNotifications(page, LanguageSetter.lang)

            // Check if response is successful
            if (response.isSuccessful) {
                // Get the body or default to an empty list
                val notifications = response.body() ?: emptyList()

                // Cache / process as needed, then return the result
                notificationsCache[page] = notifications
                Result.success(notifications)
            } else {
                // Return a failure Result with a descriptive Exception
                Result.failure(Exception("API call failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Catch network or other exceptions
            Result.failure(e)
        }
    }

    override suspend fun getNotificationDetail(id: Int): Result<String> {
        return try {
            // 1. Check cache first
            val cachedBody = notificationsBodyCache[id]
            if (cachedBody != null) {
                Result.success(cachedBody)
            } else {
                // 2. Fetch from API
                val response = retrofitInstance.api.getNotificationDetail(id, LanguageSetter.lang)
                if (response.isSuccessful) {
                    val body = response.body()?.use { it.string() } ?: ""
                    // 3. Cache result
                    notificationsBodyCache[id] = body
                    Result.success(body)
                } else {
                    Result.failure(Exception("API call failed with code: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOnBoardingNotification(): Result<Notification?> {
        return try {
            // 1. Get current preferences (blocking first emission)
            val preferences = dataStore.data.first()
            val onBoardingNotificationId = preferences[onBoardingNotificationIdKey]
            // 2. If there's an ID, fetch from the API
            if (onBoardingNotificationId != null) {
                val response = retrofitInstance.api.getOnBoardingNotification(onBoardingNotificationId.toInt())
                if (response.isSuccessful) {
                    Result.success(response.body()) // could be Notification or null
                } else {
                    Result.failure(Exception("API call failed with code: ${response.code()}"))
                }
            } else {
                // 3. No ID found — return null, or handle differently if needed
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun closedOnBoardingNotification(id: Int) {
        dataStore.edit { preferences ->
            preferences[onBoardingNotificationIdKey] = id.toString()
        }
    }

    override suspend fun getNotificationPageNumber(): Result<Int> {
        if(notificationPageNumberCache != 0) {
            return Result.success(notificationPageNumberCache)
        }

        return try {
            val response = retrofitInstance.api.getNotificationPageNumber()
            if (response.isSuccessful && response.body() != null) {
                notificationPageNumberCache = response.body()!!
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API call failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}