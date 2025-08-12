package com.asu1.appdatausecase.notification

import androidx.datastore.core.CorruptionException
import com.asu1.appdata.AppDataRepository
import com.asu1.utils.Logger
import java.io.IOException
import javax.inject.Inject

class GetNotificationPageNumberUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return try {
            appDataRepository.getNotificationPageNumber()
        } catch (e: IOException) {
            Logger.debug("I/O error getting notification page number: ${e.message}")
            Result.failure(e)
        } catch (e: CorruptionException) {
            Logger.debug("DataStore corruption when reading page number: ${e.message}")
            Result.failure(e)
        }
    }
}
