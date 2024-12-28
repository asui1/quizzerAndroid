// UserRepository.kt
package com.asu1.quizzer.repository

import com.asu1.quizzer.model.*
import retrofit2.Response


interface UserRepository {
    suspend fun guestAccount(isKo: Boolean): Response<GuestAccount>
    suspend fun login(email: String): Response<UserInfo>
    suspend fun userRequest(request: UserRequest): Response<Void>
    suspend fun deleteUser(email: String): Response<Void>
    suspend fun checkDuplicateNickname(nickname: String): Response<Void>
    suspend fun register(userInfo: UserRegister): Response<Void>
}