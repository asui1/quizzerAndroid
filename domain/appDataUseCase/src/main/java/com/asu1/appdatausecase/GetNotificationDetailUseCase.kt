package com.asu1.appdatausecase

import com.asu1.appdata.AppDataRepository
import com.asu1.utils.Logger
import javax.inject.Inject

class GetNotificationDetailUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository
) {
    suspend operator fun invoke(id: Int): Result<String> {
        return try {
            appDataRepository.getNotificationDetail(id)
        } catch (e: Exception) {
            Logger.debug("Error in GetNotificationDetailUseCase: $e")
            Result.failure(e)
        }
    }
}
