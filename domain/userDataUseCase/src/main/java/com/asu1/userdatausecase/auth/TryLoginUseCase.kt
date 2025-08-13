package com.asu1.userdatausecase.auth

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.AuthRepository
import com.asu1.userdata.UserSessionRepository
import com.asu1.utils.Logger
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TryLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(): UserLoginInfo? {
        // 1) Read local user session; bail if none
        val local = userSessionRepository.getUserLoginInfo().firstOrNull() ?: return null

        // 2) Network login (Result<UserInfo>) handled by AuthRepository/runApi
        val result = authRepository.login(local.email)

        // 3) On success, persist (await) and return local info
        return result.fold(
            onSuccess = {
                // If you want to merge server fields, do it here (e.g., nickname/urlToImage)
                userSessionRepository.saveUserLoginInfo(
                    UserLoginInfo(
                        email = local.email,
                        nickname = local.nickname,
                        urlToImage = local.urlToImage,
                        tags = local.tags ?: emptySet(), // drop ?: if non-null in your model
                        agreement = local.agreement
                    )
                )
                local
            },
            onFailure = { e ->
                Logger.debug("TryLoginUseCase login failed: ${e.message}")
                null
            }
        )
    }
}
