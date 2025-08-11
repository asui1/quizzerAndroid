package com.asu1.userdata

import com.asu1.network.AuthApi
import com.asu1.network.runApi
import com.asu1.userdatamodels.GuestAccount
import com.asu1.userdatamodels.UserInfo
import com.asu1.userdatamodels.UserRegister
import jakarta.inject.Inject
import jakarta.inject.Singleton
import retrofit2.HttpException

// 1) Auth / Account (네트워크)
interface AuthRepository {
    suspend fun guestSignIn(isKo: Boolean): Result<GuestAccount>
    suspend fun login(email: String): Result<UserInfo>
    suspend fun register(user: UserRegister): Result<Unit>
    suspend fun deleteUser(email: String): Result<Unit>
    suspend fun checkDuplicateNickname(nickname: String): Result<Unit>
}

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun guestSignIn(isKo: Boolean): Result<GuestAccount> =
        runApi { authApi.guestAccount(isKo) }
            .mapCatching { response ->
                if (!response.isSuccessful) throw HttpException(response)
                response.body() ?: throw NoSuchElementException("Empty body")
            }

    override suspend fun login(email: String): Result<UserInfo> =
        runApi { authApi.login(email) }
            .mapCatching { response ->
                if (!response.isSuccessful) throw HttpException(response)
                response.body() ?: throw NoSuchElementException("Empty body")
            }

    override suspend fun register(user: UserRegister): Result<Unit> =
        runApi { authApi.register(user) }.mapCatching { resp ->
            if (!resp.isSuccessful) throw HttpException(resp)
        }.map { }

    override suspend fun deleteUser(email: String): Result<Unit> =
        runApi { authApi.deleteUser(email) }.mapCatching { resp ->
            if (!resp.isSuccessful) throw HttpException(resp)
        }.map { }

    override suspend fun checkDuplicateNickname(nickname: String): Result<Unit> =
        runApi { authApi.checkDuplicateNickname(nickname) }.mapCatching { resp ->
            if (!resp.isSuccessful) throw HttpException(resp)
        }.map { }
}
