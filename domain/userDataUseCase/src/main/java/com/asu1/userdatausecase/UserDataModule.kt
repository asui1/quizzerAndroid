package com.asu1.userdatausecase

import com.asu1.userdata.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserDataModule {
    @Provides
    fun provideCreateGuestAccountUseCase(userRepository: UserRepository): CreateGuestAccountUseCase {
        return CreateGuestAccountUseCase(userRepository)
    }

    @Provides
    fun provideTryGuestLoginUseCase(userRepository: UserRepository): TryGuestLoginUseCase {
        return TryGuestLoginUseCase(userRepository)
    }

    @Provides
    fun provideTryLoginUseCase(userRepository: UserRepository): TryLoginUseCase {
        return TryLoginUseCase(userRepository)
    }

    @Provides
    fun provideLogOutAndGetGuestUseCase(
        userRepository: UserRepository,
        tryGuestLoginUseCase: TryGuestLoginUseCase,
        createGuestAccountUseCase: CreateGuestAccountUseCase
    ): LogOutAndGetGuestUseCase {
        return LogOutAndGetGuestUseCase(userRepository, tryGuestLoginUseCase, createGuestAccountUseCase)
    }

    @Provides
    fun provideInitLoginUseCase(
        tryLoginUseCase: TryLoginUseCase,
        tryGuestLoginUseCase: TryGuestLoginUseCase,
        createGuestAccountUseCase: CreateGuestAccountUseCase
    ): InitLoginUseCase {
        return InitLoginUseCase(tryLoginUseCase, tryGuestLoginUseCase, createGuestAccountUseCase)
    }


}