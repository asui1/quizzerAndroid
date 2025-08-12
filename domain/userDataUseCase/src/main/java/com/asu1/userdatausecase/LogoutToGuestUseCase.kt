package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.UserSessionRepository
import javax.inject.Inject

class LogoutToGuestUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository,
    private val tryGuestLoginUseCase: TryGuestLoginUseCase,
) {
    suspend operator fun invoke(): UserLoginInfo? {
        // wipe only the *user* session; guest prefs remain
        userSessionRepository.clearUserLoginInfo()
        // try to use an existing guest session; if none, return null
        return tryGuestLoginUseCase()
    }
}
