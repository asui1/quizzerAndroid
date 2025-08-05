package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.UserRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class TryGuestLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): UserLoginInfo? = coroutineScope {
        val guestAccount = userRepository.getGuestLoginInfo().firstOrNull()
        if(guestAccount != null) {
            val loginResult = userRepository.login(guestAccount.email)
            if (loginResult.isSuccessful) {
                launch {
                    userRepository.saveUserLoginInfo(
                        email = guestAccount.email,
                        nickname = guestAccount.nickname,
                        urlToImage = guestAccount.urlToImage,
                        tags = guestAccount.tags ?: emptySet(),
                        agreement = guestAccount.agreement
                    )
                }
                return@coroutineScope guestAccount
            }
        }
        return@coroutineScope null
    }
}
