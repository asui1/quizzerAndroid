package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.UserRepository
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class LogoutToGuestUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tryGuestLoginUseCase: TryGuestLoginUseCase,
    private val createGuestAccountUseCase: CreateGuestAccountUseCase,
) {
    suspend operator fun invoke(): UserLoginInfo? = coroutineScope {
        userRepository.clearUserLoginInfo()

        tryGuestLoginUseCase().run {
            if (this != null) {
                return@coroutineScope this
            }
        }

        return@coroutineScope null
    }

}