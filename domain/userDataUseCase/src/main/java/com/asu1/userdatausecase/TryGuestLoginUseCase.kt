package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.AuthRepository
import com.asu1.userdata.UserSessionRepository
import com.asu1.utils.Logger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class TryGuestLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(): UserLoginInfo? {
        // 1) get guest session (if none, stop here)
        val guest = userSessionRepository.getGuestLoginInfo().firstOrNull() ?: return null

        // 2) try logging in with guest email via AuthRepository (Result-based)
        val login = authRepository.login(guest.email)

        return login.fold(
            onSuccess = {
                // 3) persist as a *user* session (await, not fire-and-forget)
                userSessionRepository.saveUserLoginInfo(
                    UserLoginInfo(
                        email = guest.email,
                        nickname = guest.nickname,
                        urlToImage = guest.urlToImage,
                        tags = guest.tags ?: emptySet(),
                        agreement = guest.agreement
                    )
                )
                guest
            },
            onFailure = { e ->
                Logger.debug("TryGuestLoginUseCase login failed: ${e.message}")
                null
            }
        )
    }
}
