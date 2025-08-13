package com.asu1.userdata

import android.net.TrafficStats
import com.asu1.network.AuthApi
import com.asu1.network.IoDispatcher
import com.asu1.network.runApi
import com.asu1.resources.NetworkTags
import com.asu1.userdatamodels.GuestAccount
import com.asu1.userdatamodels.UserInfo
import com.asu1.userdatamodels.UserRegister
import com.asu1.userdatamodels.UserRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

// 1) Auth / Account (네트워크)
interface AuthRepository {
    suspend fun guestSignIn(isKo: Boolean): Result<GuestAccount>
    suspend fun login(email: String): Result<UserInfo>
    suspend fun register(user: UserRegister): Result<Unit>
    suspend fun deleteUser(email: String): Result<Unit>
    suspend fun checkDuplicateNickname(nickname: String): Result<Unit>
    suspend fun sendInquiry(email: String, subject: String, body: String): Result<Unit>
}

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    @IoDispatcher private val io: CoroutineDispatcher
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

    override suspend fun sendInquiry(
        email: String,
        subject: String,
        body: String
    ): Result<Unit> = withContext(io) {
        TrafficStats.setThreadStatsTag(NetworkTags.SEND_INQUIRY)
        try {
            val req = UserRequest(email = email, subject = subject, body = body)
            runApi { authApi.userRequest(req) }              // Result<Response<Void>>
                .mapCatching { resp ->
                    if (!resp.isSuccessful) throw HttpException(resp)
                }
                .map { }                                      // Result<Unit>
        } finally {
            TrafficStats.clearThreadStatsTag()
        }
    }
}
