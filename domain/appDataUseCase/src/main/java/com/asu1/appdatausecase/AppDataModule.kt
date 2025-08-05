package com.asu1.appdatausecase

import com.asu1.appdata.AppDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppDataModule {
    @Provides
    fun provideGetNotificationUseCase(appDataRepository: AppDataRepository): GetNotificationUseCase {
        return GetNotificationUseCase(appDataRepository)
    }

    @Provides
    fun provideGetNotificationDetailUseCase(appDataRepository: AppDataRepository): GetNotificationDetailUseCase {
        return GetNotificationDetailUseCase(appDataRepository)
    }

    @Provides
    fun provideGetOnBoardingNotificationUseCase(appDataRepository: AppDataRepository):
            GetOnBoardingNotificationUseCase {
        return GetOnBoardingNotificationUseCase(appDataRepository)
    }

    @Provides
    fun provideCloseOnBoardingNotificationUseCase(appDataRepository: AppDataRepository):
            CloseOnBoardingNotificationUseCase {
        return CloseOnBoardingNotificationUseCase(appDataRepository)
    }

    @Provides
    fun provideGetNotificationPageNumber(appDataRepository: AppDataRepository):
            GetNotificationPageNumberUseCase {
        return GetNotificationPageNumberUseCase(appDataRepository)
    }
}
