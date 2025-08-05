package com.asu1.appdatausecase

import com.asu1.appdata.AppDataRepository
import com.asu1.appdatamodels.Notification
import com.asu1.utils.Logger
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository
) {
    suspend operator fun invoke(page: Int): Result<List<Notification>> {
        return try {
            appDataRepository.getNotification(page)
        } catch (e: IOException) {
            Logger.debug("Network I/O error fetching notifications for page $page: ${e.message}")
            Result.failure(e)
        } catch (e: HttpException) {
            Logger.debug("HTTP ${e.code()} error fetching notifications for page $page")
            Result.failure(e)
        }
    }
}
