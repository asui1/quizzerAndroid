// UserRepository.kt
package com.asu1.userdata

import android.content.Context
import com.asu1.network.RetrofitInstance
import com.asu1.resources.UserLoginInfo
import com.asu1.userdatamodels.GuestAccount
import com.asu1.userdatamodels.UserInfo
import com.asu1.userdatamodels.UserRegister
import com.asu1.userdatamodels.UserRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Singleton

interface UserRepository {
    suspend fun guestAccount(isKo: Boolean): Response<GuestAccount>
    suspend fun login(email: String): Response<UserInfo>
    suspend fun userRequest(request: UserRequest): Response<Void>
    suspend fun deleteUser(email: String): Response<Void>
    suspend fun checkDuplicateNickname(nickname: String): Response<Void>
    suspend fun register(userInfo: UserRegister): Response<Void>
    suspend fun saveUserLoginInfo(email: String, nickname: String, urlToImage: String?, tags: Set<String>, agreement: Boolean)
    fun getUserLoginInfo(): Flow<UserLoginInfo?>
    suspend fun saveGuestLoginInfo(email: String, nickname: String, urlToImage: String?, tags: Set<String>, agreement: Boolean)
    fun getGuestLoginInfo(): Flow<UserLoginInfo?>
    suspend fun clearUserLoginInfo()
}

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext context: Context,
        retrofitInstance: RetrofitInstance
    ): UserRepository {
        return UserRepositoryImpl(context, retrofitInstance)
    }

//    Binds가 더 맞는 구현일듯. Provide보다는.
//    @Binds
//    @Singleton
//    abstract fun bindUserRepository(
//        userRepositoryImpl: UserRepositoryImpl
//    ): UserRepository
}