package com.asu1.userdatausecase.auth

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.AuthRepository
import com.asu1.userdata.UserSessionRepository
import com.asu1.utils.Logger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateGuestAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(isKo: Boolean): UserLoginInfo? = coroutineScope {
        authRepository.guestSignIn(isKo).fold(
            onSuccess = { guest ->
                val info = UserLoginInfo(
                    email = guest.email,
                    nickname = guest.nickname,
                    urlToImage = null,
                    tags = emptySet(),
                    agreement = false
                )
                // fire-and-forget local save
                launch { userSessionRepository.saveGuestLoginInfo(info) }
                info
            },
            onFailure = { e ->
                Logger.debug("CreateGuestAccountUseCase failed: ${e.message}")
                null
            }
        )
    }
}