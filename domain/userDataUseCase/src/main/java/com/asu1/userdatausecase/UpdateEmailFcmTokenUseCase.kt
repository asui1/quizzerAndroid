package com.asu1.userdatausecase

import com.asu1.userdata.PushRepository
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UpdateEmailFcmTokenUseCase @Inject constructor(
    private val userRepository: PushRepository
) {
    suspend operator fun invoke(): Result<Unit> = coroutineScope {
        return@coroutineScope userRepository.sendEmailToken()
    }
}
