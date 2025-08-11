package com.asu1.appdata

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.asu1.appdatamodels.Notification
import com.asu1.network.RetrofitInstance
import com.asu1.network.runApi
import com.asu1.utils.LanguageSetter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
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
        // 1) 캐시 먼저
        notificationsCache[page]?.let { return Result.success(it) }

        // 2) API 호출 + 응답 검증 (예외 매핑은 runApi가 담당)
        val result: Result<List<Notification>> =
            runApi { retrofitInstance.notificationApi.getNotifications(page, LanguageSetter.lang) }
                .mapCatching { response ->
                    if (!response.isSuccessful) throw HttpException(response)
                    response.body() ?: emptyList()
                }

        // 3) 성공 시 캐싱
        result.onSuccess { notifications -> notificationsCache[page] = notifications }
        return result
    }

    override suspend fun getNotificationDetail(id: Int): Result<String> {
        // 1) 캐시 히트 시 즉시 반환
        notificationsBodyCache[id]?.let { return Result.success(it) }

        // 2) API 호출 (예외는 runApi가 Result.failure로 매핑)
        val result: Result<String> =
            runApi { retrofitInstance.notificationApi.getNotificationDetail(id, LanguageSetter.lang) }
                .mapCatching { response ->
                    if (!response.isSuccessful) throw HttpException(response)
                    response.body()?.use { it.string() }
                        ?: throw NoSuchElementException("Empty body")
                }

        // 3) 성공 시 캐싱
        result.onSuccess { body -> notificationsBodyCache[id] = body }
        return result
    }

    override suspend fun getOnBoardingNotification(): Result<Notification?> {
        val result: Result<Notification?> = runApi { dataStore.data.first()[onBoardingNotificationIdKey] }
            .fold(
                onSuccess = { id ->
                    if (id.isNullOrBlank()) {
                        Result.success(null)
                    } else {
                        runApi { retrofitInstance.notificationApi.getOnBoardingNotification(id.toInt()) }
                            .mapCatching { resp ->
                                if (!resp.isSuccessful) throw HttpException(resp)
                                resp.body()
                            }
                    }
                },
                onFailure = { Result.failure(it) }
            )
        return result
    }

    override suspend fun closedOnBoardingNotification(id: Int) {
        dataStore.edit { preferences ->
            preferences[onBoardingNotificationIdKey] = id.toString()
        }
    }

    override suspend fun getNotificationPageNumber(): Result<Int> {
        // 1) 캐시 히트면 즉시 반환
        if (notificationPageNumberCache != 0) {
            return Result.success(notificationPageNumberCache)
        }

        // 2) API 호출 (예외는 runApi에서 Result.failure로 매핑)
        val result: Result<Int> =
            runApi { retrofitInstance.notificationApi.getNotificationPageNumber() }
                .mapCatching { response ->
                    if (!response.isSuccessful) throw HttpException(response)
                    response.body() ?: throw NoSuchElementException("Empty body")
                }

        // 3) 성공 시 캐싱
        result.onSuccess { pageNum -> notificationPageNumberCache = pageNum }
        return result
    }
}
