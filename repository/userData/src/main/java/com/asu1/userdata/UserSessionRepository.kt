package com.asu1.userdata

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.asu1.resources.UserLoginInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface UserSessionRepository {
    suspend fun saveUserLoginInfo(info: UserLoginInfo)
    fun getUserLoginInfo(): Flow<UserLoginInfo?>
    suspend fun saveGuestLoginInfo(info: UserLoginInfo)
    fun getGuestLoginInfo(): Flow<UserLoginInfo?>
    suspend fun clearUserLoginInfo()
}

@Singleton
class UserSessionRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : UserSessionRepository {

    private val Context.dataStore by preferencesDataStore(name = "user_session_prefs")
    private val dataStore = context.dataStore

    private val emailKey = stringPreferencesKey("email")
    private val nicknameKey = stringPreferencesKey("nickname")
    private val urlToImageKey = stringPreferencesKey("urlToImage")
    private val tagsKey = stringPreferencesKey("tags")
    private val agreementKey = stringPreferencesKey("agreement")

    private val guestEmailKey = stringPreferencesKey("guestEmail")
    private val guestNicknameKey = stringPreferencesKey("guestNickname")
    private val guestUrlToImageKey = stringPreferencesKey("guestUrlToImage")
    private val guestTagsKey = stringPreferencesKey("guestTags")
    private val guestAgreementKey = stringPreferencesKey("guestAgreement")

    override suspend fun saveUserLoginInfo(info: UserLoginInfo) {
        dataStore.edit {
            it[emailKey] = info.email
            it[nicknameKey] = info.nickname
            it[urlToImageKey] = info.urlToImage.orEmpty()
            it[tagsKey] = info.tags?.joinToString(",") ?: ""
            it[agreementKey] = info.agreement.toString()
        }
    }

    override fun getUserLoginInfo(): Flow<UserLoginInfo?> =
        dataStore.data.map { p ->
            p[emailKey]?.let {
                UserLoginInfo(
                    email = it,
                    nickname = p[nicknameKey].orEmpty(),
                    urlToImage = p[urlToImageKey].orEmpty(),
                    tags = p[tagsKey]?.split(",")?.filter { s -> s.isNotBlank() }?.toSet() ?: emptySet(),
                    agreement = p[agreementKey]?.toBoolean() ?: false
                )
            }
        }

    override suspend fun saveGuestLoginInfo(info: UserLoginInfo) {
        dataStore.edit {
            it[guestEmailKey] = info.email
            it[guestNicknameKey] = info.nickname
            it[guestUrlToImageKey] = info.urlToImage.orEmpty()
            it[guestTagsKey] = info.tags?.joinToString(",") ?: ""
            it[guestAgreementKey] = info.agreement.toString()
        }
    }

    override fun getGuestLoginInfo(): Flow<UserLoginInfo?> =
        dataStore.data.map { p ->
            p[guestEmailKey]?.let {
                UserLoginInfo(
                    email = it,
                    nickname = p[guestNicknameKey].orEmpty(),
                    urlToImage = p[guestUrlToImageKey].orEmpty(),
                    tags = p[guestTagsKey]?.split(",")?.filter { s -> s.isNotBlank() }?.toSet() ?: emptySet(),
                    agreement = p[guestAgreementKey]?.toBoolean() ?: false
                )
            }
        }

    override suspend fun clearUserLoginInfo() {
        dataStore.edit {
            it.remove(emailKey); it.remove(nicknameKey); it.remove(urlToImageKey)
            it.remove(tagsKey); it.remove(agreementKey)
        }
    }
}
