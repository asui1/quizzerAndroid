package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.UserRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.logging.Logger
import javax.inject.Inject

class CreateGuestAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(isKo: Boolean) : UserLoginInfo? = coroutineScope {
        try {
            val guestAccountResult = userRepository.guestAccount(isKo)
            Logger.getLogger("CreateGuestAccountUseCase").info("Guest account creation result: ${guestAccountResult.isSuccessful}")
            Logger.getLogger("CreateGuestAccountUseCase").info("Guest account creation result: ${guestAccountResult.raw()}")
            if (guestAccountResult.isSuccessful) {
                Logger.getLogger("CreateGuestAccountUseCase").info("Guest account created")
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
            Logger.getLogger("CreateGuestAccountUseCase").info("Guest account creation failed")
            return@coroutineScope null
        } catch (e: Exception) {
            Logger.getLogger("CreateGuestAccountUseCase").info("Guest account creation failed ${e.message}")
            return@coroutineScope null
        }
    }
}