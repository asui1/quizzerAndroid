package com.asu1.userdatausecase

import com.asu1.userdata.UserRepository
import com.asu1.utils.Logger
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SaveFcmTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(token: String): Result<Unit> {
        return try {
            userRepository.saveFcmToken(token)
            userRepository.sendEmailToken()
            Result.success(Unit)
        } catch (e: IOException) {
            Logger.debug("I/O error saving FCM token: ${e.message}")
            Result.failure(e)
        } catch (e: HttpException) {
            Logger.debug("HTTP ${e.code()} error sending email token: ${e.message}")
            Result.failure(e)
        }
    }
}
