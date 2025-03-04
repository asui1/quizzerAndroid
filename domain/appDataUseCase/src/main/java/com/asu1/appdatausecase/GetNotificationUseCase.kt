package com.asu1.appdatausecase

import com.asu1.appdata.AppDataRepository
import com.asu1.appdatamodels.Notification
import javax.inject.Inject
import com.asu1.utils.Logger

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