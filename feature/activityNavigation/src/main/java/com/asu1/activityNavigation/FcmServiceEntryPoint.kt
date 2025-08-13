package com.asu1.activityNavigation

import com.asu1.userdatausecase.session.SaveFcmTokenUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FcmServiceEntryPoint {
    fun saveFcmTokenUseCase(): SaveFcmTokenUseCase
    @Suppress("unused")
    fun sendEmailTokenUseCase(): SaveFcmTokenUseCase
}
