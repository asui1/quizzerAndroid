package com.asu1.userdatausecase

import com.asu1.userdata.UserRepository
import com.asu1.utils.Logger
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SaveFcmTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(token: String) : Result<Unit> = coroutineScope {
        try {
            userRepository.saveFcmToken(token)
            userRepository.sendEmailToken()
            return@coroutineScope Result.success(Unit)
        }catch (e: Exception){
            Logger.debug("Save Fcm Token Failed: $e")
            return@coroutineScope Result.failure(Exception(e))
        }
    }
}