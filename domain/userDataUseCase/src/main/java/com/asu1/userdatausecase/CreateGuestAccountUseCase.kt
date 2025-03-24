package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.UserRepository
import com.asu1.utils.Logger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateGuestAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(isKo: Boolean) : UserLoginInfo? = coroutineScope {
        try {
            val guestAccountResult = userRepository.guestAccount(isKo)
            if (guestAccountResult.isSuccessful) {
                launch {
                    userRepository.saveGuestLoginInfo(
                        email = guestAccountResult.body()!!.email,
                        nickname = guestAccountResult.body()!!.nickname,
                        urlToImage = null,
                        tags = emptySet(),
                        agreement = false
                    )
                }
                return@coroutineScope UserLoginInfo(
                    email = guestAccountResult.body()!!.email,
                    nickname = guestAccountResult.body()!!.nickname,
                    urlToImage = null,
                    tags = emptySet(),
                    agreement = false
                )
            }
            return@coroutineScope null
        } catch (e: Exception) {
            Logger.debug("Error in CreateGuestAccountUseCase $e")
            return@coroutineScope null
        }
    }
}