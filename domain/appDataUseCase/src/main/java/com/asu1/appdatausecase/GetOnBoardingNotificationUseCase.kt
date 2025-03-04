package com.asu1.appdatausecase

import com.asu1.appdata.AppDataRepository
import com.asu1.appdatamodels.Notification
import com.asu1.utils.Logger
import javax.inject.Inject

class GetOnBoardingNotificationUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository
) {
    suspend operator fun invoke(): Result<Notification?> {
        return try {
            appDataRepository.getOnBoardingNotification()
        } catch (e: Exception) {
            Logger.debug("Error in GetOnBoardingNotificationUseCase: $e")
            Result.failure(e)
        }
    }
}
