package com.asu1.userdatausecase

import com.asu1.userdata.ActivityRepository
import com.asu1.userdata.AuthRepository
import com.asu1.userdata.PushRepository
import com.asu1.userdata.UserSessionRepository
import com.asu1.userdatausecase.auth.CreateGuestAccountUseCase
import com.asu1.userdatausecase.auth.InitLoginUseCase
import com.asu1.userdatausecase.auth.LogOutAndGetGuestUseCase
import com.asu1.userdatausecase.auth.LoginWithEmailUseCase
import com.asu1.userdatausecase.auth.LogoutToGuestUseCase
import com.asu1.userdatausecase.auth.TryGuestLoginUseCase
import com.asu1.userdatausecase.auth.TryLoginUseCase
import com.asu1.userdatausecase.session.SaveFcmTokenUseCase
import com.asu1.userdatausecase.session.UpdateEmailFcmTokenUseCase
import com.asu1.userdatausecase.user.GetUserActivitiesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserDataModule {

    /* ---------- Auth + Session ---------- */

    @Provides
    fun provideCreateGuestAccountUseCase(
        authRepository: AuthRepository,
        userSessionRepository: UserSessionRepository
    ): CreateGuestAccountUseCase =
        CreateGuestAccountUseCase(authRepository, userSessionRepository)

    @Provides
    fun provideTryGuestLoginUseCase(
        authRepository: AuthRepository,
        userSessionRepository: UserSessionRepository
    ): TryGuestLoginUseCase =
        TryGuestLoginUseCase(authRepository, userSessionRepository)

    @Provides
    fun provideTryLoginUseCase(
        authRepository: AuthRepository,
        userSessionRepository: UserSessionRepository
    ): TryLoginUseCase =
        TryLoginUseCase(authRepository, userSessionRepository)

    @Provides
    fun provideLoginWithEmailUseCase(
        authRepository: AuthRepository,
        userSessionRepository: UserSessionRepository
    ): LoginWithEmailUseCase =
        LoginWithEmailUseCase(authRepository, userSessionRepository)

    /* ---------- Orchestration ---------- */

    @Provides
    fun provideLogOutAndGetGuestUseCase(
        userSessionRepository: UserSessionRepository,
        tryGuestLoginUseCase: TryGuestLoginUseCase,
        createGuestAccountUseCase: CreateGuestAccountUseCase
    ): LogOutAndGetGuestUseCase =
        LogOutAndGetGuestUseCase(
            userSessionRepository,
            tryGuestLoginUseCase,
            createGuestAccountUseCase
        )

    @Provides
    fun provideInitLoginUseCase(
        tryLoginUseCase: TryLoginUseCase,
        tryGuestLoginUseCase: TryGuestLoginUseCase,
        createGuestAccountUseCase: CreateGuestAccountUseCase
    ): InitLoginUseCase =
        InitLoginUseCase(tryLoginUseCase, tryGuestLoginUseCase, createGuestAccountUseCase)

    @Provides
    fun provideLogoutToGuestUseCase(
        userSessionRepository: UserSessionRepository,
        tryGuestLoginUseCase: TryGuestLoginUseCase,
    ): LogoutToGuestUseCase =
        LogoutToGuestUseCase(
            userSessionRepository,
            tryGuestLoginUseCase,
        )

    /* ---------- Activity ---------- */

    @Provides
    fun provideGetUserActivitiesUseCase(
        activityRepository: ActivityRepository
    ): GetUserActivitiesUseCase =
        GetUserActivitiesUseCase(activityRepository)

    /* ---------- Push/FCM ---------- */

    @Provides
    fun provideSaveFcmTokenUseCase(
        pushRepository: PushRepository
    ): SaveFcmTokenUseCase =
        SaveFcmTokenUseCase(pushRepository)

    @Provides
    fun provideUpdateEmailFcmTokenUseCase(
        pushRepository: PushRepository
    ): UpdateEmailFcmTokenUseCase =
        UpdateEmailFcmTokenUseCase(pushRepository)

    // If you also added the sendEmailToken() orchestration as a separate use case:
    // @Provides
    // fun provideSendEmailTokenUseCase(pushRepository: PushRepository): SendEmailTokenUseCase =
    //     SendEmailTokenUseCase(pushRepository)
}
