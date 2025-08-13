package com.asu1.userdatausecase.auth

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.AuthRepository
import com.asu1.userdata.UserSessionRepository
import com.asu1.utils.Logger
import javax.inject.Inject

class LoginWithEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(email: String, profileUri: String): UserLoginInfo? {
        if (email.isBlank()) return null

        val result = authRepository.login(email)

        return result.fold(
            onSuccess = { userInfo ->
                val info = UserLoginInfo(
                    email = email,
                    nickname = userInfo.nickname,
                    urlToImage = profileUri,                 // keep provided profile image
                    tags = userInfo.tags,      // drop ?: if non-null in your model
                    agreement = false
                )
                // persist before returning (safer than fire-and-forget)
                userSessionRepository.saveUserLoginInfo(info)
                info
            },
            onFailure = { e ->
                Logger.debug("LoginWithEmailUseCase failed: ${e.message}")
                null
            }
        )
    }
}