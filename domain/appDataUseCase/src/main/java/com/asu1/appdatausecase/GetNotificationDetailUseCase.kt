package com.asu1.appdatausecase

import com.asu1.appdata.AppDataRepository
import com.asu1.utils.Logger
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetNotificationDetailUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository
) {
    suspend operator fun invoke(id: Int): Result<String> {
        return try {
            appDataRepository.getNotificationDetail(id)
        } catch (e: IOException) {
            Logger.debug("Network error fetching notification $id: ${e.message}")
            Result.failure(e)
        } catch (e: HttpException) {
            Logger.debug("HTTP ${e.code()} error fetching notification $id")
            Result.failure(e)
        }
    }
}
