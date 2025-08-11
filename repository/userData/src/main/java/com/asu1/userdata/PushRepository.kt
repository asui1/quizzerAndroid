package com.asu1.userdata

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.asu1.network.AuthApi
import com.asu1.network.runApi
import com.asu1.userdatamodels.FcmToken
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException

interface PushRepository {
    suspend fun updateFcmTokenRemote(email: String, token: String): Result<Unit>
    suspend fun saveFcmTokenLocal(token: String)
    fun getFcmTokenLocal(): String
    suspend fun sendEmailToken(): Result<Unit>
}

@Singleton
class PushRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,                      // updateFcmToken(...) 제공
    private val userSessionRepository: UserSessionRepository, // to read current email
    @ApplicationContext context: Context
) : PushRepository {

    private val Context.dataStore by preferencesDataStore("user_prefs")
    private val dataStore = context.dataStore
    private val fcmTokenKey = stringPreferencesKey("fcmToken")

    // 즉시 응답을 위해 캐시 유지
    @Volatile private var fcmTokenCache: String = ""

    init {
        // 앱 생명주기 동안 캐시 동기화
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            dataStore.data.map { it[fcmTokenKey].orEmpty() }.collect { fcmTokenCache = it }
        }
    }

    override suspend fun updateFcmTokenRemote(email: String, token: String): Result<Unit> =
        runApi { authApi.updateFcmToken(FcmToken(userEmail = email, fcmToken = token)) }
            .mapCatching { resp ->
                if (!resp.isSuccessful) throw HttpException(resp)
            }.map { }

    override suspend fun saveFcmTokenLocal(token: String) {
        dataStore.edit { it[fcmTokenKey] = token }
        fcmTokenCache = token
    }

    override fun getFcmTokenLocal(): String = fcmTokenCache

    override suspend fun sendEmailToken(): Result<Unit> {
        val token = getFcmTokenLocal().takeIf { it.isNotBlank() }
        val email = userSessionRepository.getUserLoginInfo().firstOrNull()?.email
            ?: userSessionRepository.getGuestLoginInfo().firstOrNull()?.email
        return when {
            token == null -> Result.failure(IllegalStateException("No Token Exists"))
            email == null -> Result.failure(IllegalStateException("No User Email Exists"))
            else ->updateFcmTokenRemote(email, token)
        }
    }
}
