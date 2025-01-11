// UserRepository.kt
package com.asu1.quizzer.repository

import com.asu1.quizzer.model.GuestAccount
import com.asu1.quizzer.model.UserInfo
import com.asu1.quizzer.model.UserRegister
import com.asu1.quizzer.model.UserRequest
import com.asu1.quizzer.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import javax.inject.Singleton

interface UserRepository {
    suspend fun guestAccount(isKo: Boolean): Response<GuestAccount>
    suspend fun login(email: String): Response<UserInfo>
    suspend fun userRequest(request: UserRequest): Response<Void>
    suspend fun deleteUser(email: String): Response<Void>
    suspend fun checkDuplicateNickname(nickname: String): Response<Void>
    suspend fun register(userInfo: UserRegister): Response<Void>
}

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepositoryImpl(apiService)
    }
}