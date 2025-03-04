package com.asu1.appdatausecase

import com.asu1.appdata.AppDataRepository
import com.asu1.utils.Logger
import javax.inject.Inject

class CloseOnBoardingNotificationUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return try {
            appDataRepository.getNotificationPageNumber()
        } catch (e: Exception) {
            Logger.debug("Error in GetNotificationDetailUseCase: $e")
            Result.failure(e)
        }
    }
}
