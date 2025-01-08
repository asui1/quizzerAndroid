package com.asu1.quizzer.repository

import com.asu1.quizzer.model.GuestAccount
import com.asu1.quizzer.model.UserInfo
import com.asu1.quizzer.model.UserRegister
import com.asu1.quizzer.model.UserRequest
import com.asu1.quizzer.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun guestAccount(isKo: Boolean): Response<GuestAccount> {
        return apiService.guestAccount(isKo)
    }

    override suspend fun login(email: String): Response<UserInfo> {
        return apiService.login(email)
    }

    override suspend fun userRequest(request: UserRequest): Response<Void> {
        return apiService.userRequest(request)
    }

    override suspend fun deleteUser(email: String): Response<Void> {
        return apiService.deleteUser(email)
    }

    override suspend fun checkDuplicateNickname(nickname: String): Response<Void> {
        return apiService.checkDuplicateNickname(nickname)
    }

    override suspend fun register(userInfo: UserRegister): Response<Void> {
        return apiService.register(userInfo)
    }
}