package com.asu1.appdatausecase

import androidx.datastore.core.CorruptionException
import com.asu1.appdata.AppDataRepository
import com.asu1.appdatamodels.Notification
import com.asu1.utils.Logger
import java.io.IOException
import javax.inject.Inject

class GetOnBoardingNotificationUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository
) {
    suspend operator fun invoke(): Result<Notification?> {
        return try {
            appDataRepository.getOnBoardingNotification()
        } catch (e: IOException) {
            Logger.debug("I/O error getting onboarding notification: ${e.message}")
            Result.failure(e)
        } catch (e: CorruptionException) {
            Logger.debug("DataStore corruption reading onboarding notification: ${e.message}")
            Result.failure(e)
        }
    }
}
