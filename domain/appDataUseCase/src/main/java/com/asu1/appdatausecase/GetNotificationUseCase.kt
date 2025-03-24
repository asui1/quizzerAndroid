package com.asu1.appdatausecase

import com.asu1.appdata.AppDataRepository
import com.asu1.appdatamodels.Notification
import com.asu1.utils.Logger
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository
) {
    suspend operator fun invoke(page: Int): Result<List<Notification>> {
        return try {
            appDataRepository.getNotification(page)
        } catch (e: Exception) {
            Logger.debug("Error in GetNotificationUseCase: $e")
            Result.failure(e)
        }
    }
}