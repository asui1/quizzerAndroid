package com.asu1.appdata

import android.content.Context
import com.asu1.appdatamodels.Notification
import com.asu1.network.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface AppDataRepository {
    suspend fun getNotification(page: Int): Result<List<Notification>>
    suspend fun getNotificationDetail(id: Int): Result<String>
    suspend fun getNotificationPageNumber(): Result<Int>
    suspend fun getOnBoardingNotification(): Result<Notification?>
    suspend fun closedOnBoardingNotification(id: Int)
}

@Module
@InstallIn(SingletonComponent::class)
object AppDataRepositoryModule {

    @Provides
    @Singleton
    fun provideAppDataRepository(
        @ApplicationContext context: Context,
        retrofitInstance: RetrofitInstance
    ): AppDataRepository {
        return AppDataRepositoryImpl(context, retrofitInstance)
    }
}