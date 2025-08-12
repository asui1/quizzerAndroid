package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.UserSessionRepository
import javax.inject.Inject

class LogOutAndGetGuestUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository,
    private val tryGuestLoginUseCase: TryGuestLoginUseCase,
    private val createGuestAccountUseCase: CreateGuestAccountUseCase
) {
    suspend operator fun invoke(isKo: Boolean): UserLoginInfo? {
        // 1) Clear current *user* session (guest info remains for fallback)
        userSessionRepository.clearUserLoginInfo()

        // 2) If a guest session already exists, try to use it
        tryGuestLoginUseCase()?.let { return it }

        // 3) Otherwise, create a new guest account and persist it
        return createGuestAccountUseCase(isKo)
    }
}
