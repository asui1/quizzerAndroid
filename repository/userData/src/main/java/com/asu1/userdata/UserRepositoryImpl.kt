package com.asu1.userdata

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.asu1.network.RetrofitInstance
import com.asu1.resources.UserLoginInfo
import com.asu1.userdatamodels.GuestAccount
import com.asu1.userdatamodels.UserInfo
import com.asu1.userdatamodels.UserRegister
import com.asu1.userdatamodels.UserRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val retrofitInstance: RetrofitInstance,
) : UserRepository {
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")

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

    private val dataStore = context.dataStore

    override suspend fun guestAccount(isKo: Boolean): Response<GuestAccount> {
        return retrofitInstance.api.guestAccount(isKo)
    }

    override suspend fun login(email: String): Response<UserInfo> {
        return retrofitInstance.api.login(email)
    }

    override suspend fun userRequest(request: UserRequest): Response<Void> {
        return retrofitInstance.api.userRequest(request)
    }

    override suspend fun deleteUser(email: String): Response<Void> {
        return retrofitInstance.api.deleteUser(email)
    }

    override suspend fun checkDuplicateNickname(nickname: String): Response<Void> {
        return retrofitInstance.api.checkDuplicateNickname(nickname)
    }

    override suspend fun register(userInfo: UserRegister): Response<Void> {
        return retrofitInstance.api.register(userInfo)
    }

    override suspend fun clearUserLoginInfo() {
        dataStore.edit { preferences ->
            preferences.remove(emailKey)
            preferences.remove(nicknameKey)
            preferences.remove(urlToImageKey)
            preferences.remove(tagsKey)
            preferences.remove(agreementKey)
        }
    }

    override suspend fun saveUserLoginInfo(
        email: String,
        nickname: String,
        urlToImage: String?,
        tags: Set<String>,
        agreement: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[emailKey] = email
            preferences[nicknameKey] = nickname
            preferences[urlToImageKey] = urlToImage ?: ""
            preferences[tagsKey] = tags.joinToString(",")
            preferences[agreementKey] = agreement.toString()
        }
    }

    override fun getUserLoginInfo(): Flow<UserLoginInfo?> {
        return dataStore.data.map { preferences ->
            val email = preferences[emailKey]
            if (email != null) {
                UserLoginInfo(
                    email = email,
                    nickname = preferences[nicknameKey] ?: "",
                    urlToImage = preferences[urlToImageKey] ?: "",
                    tags = preferences[tagsKey]?.split(",")?.toSet() ?: emptySet(),
                    agreement = preferences[agreementKey]?.toBoolean() ?: false
                )
            } else {
                null
            }
        }
    }

    override suspend fun saveGuestLoginInfo(
        email: String,
        nickname: String,
        urlToImage: String?,
        tags: Set<String>,
        agreement: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[guestEmailKey] = email
            preferences[guestNicknameKey] = nickname
            preferences[guestUrlToImageKey] = urlToImage ?: ""
            preferences[guestTagsKey] = tags.joinToString(",")
            preferences[guestAgreementKey] = agreement.toString()
        }
    }

    override fun getGuestLoginInfo(): Flow<UserLoginInfo?> {
        return dataStore.data.map { preferences ->
            val email = preferences[guestEmailKey]
            if (email != null) {
                UserLoginInfo(
                    email = email,
                    nickname = preferences[guestNicknameKey] ?: "",
                    urlToImage = preferences[guestUrlToImageKey] ?: "",
                    tags = preferences[guestTagsKey]?.split(",")?.toSet() ?: emptySet(),
                    agreement = preferences[guestAgreementKey]?.toBoolean() ?: false
                )
            } else {
                null
            }
        }
    }

}