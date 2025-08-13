package com.asu1.userdatausecase.session

import com.asu1.userdata.PushRepository
import com.asu1.utils.Logger
import javax.inject.Inject

class SaveFcmTokenUseCase @Inject constructor(
    private val pushRepository: PushRepository
) {
    suspend operator fun invoke(token: String): Result<Unit> {
        val normalized = token.trim()
        if (normalized.isEmpty()) {
            Logger.debug("SaveFcmTokenUseCase: token is empty")
            return Result.failure(IllegalArgumentException("FCM token must not be empty"))
        }

        // Save locally first; if that fails, surface the error
        return runCatching { pushRepository.saveFcmTokenLocal(normalized) }
            .fold(
                onSuccess = {
                    // Then try to send (PushRepository.sendEmailToken() returns Result<Unit>)
                    pushRepository.sendEmailToken()
                },
                onFailure = { e ->
                    Logger.debug("SaveFcmTokenUseCase: local save failed: ${e.message}")
                    Result.failure(e)
                }
            )
    }
}
