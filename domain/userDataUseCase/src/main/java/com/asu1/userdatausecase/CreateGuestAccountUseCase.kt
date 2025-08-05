package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.UserRepository
import com.asu1.utils.Logger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CreateGuestAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(isKo: Boolean): UserLoginInfo? = coroutineScope {
        try {
            val response = userRepository.guestAccount(isKo)
            if (!response.isSuccessful) return@coroutineScope null

            // safe non-null body
            val body = response.body() ?: return@coroutineScope null

            // fire‐and‐forget saving the info
            launch {
                userRepository.saveGuestLoginInfo(
                    email      = body.email,
                    nickname   = body.nickname,
                    urlToImage = null,
                    tags       = emptySet(),
                    agreement  = false
                )
            }

            // return the newly created info
            UserLoginInfo(
                email      = body.email,
                nickname   = body.nickname,
                urlToImage = null,
                tags       = emptySet(),
                agreement  = false
            )

        } catch (e: IOException) {
            Logger.debug("Network I/O error in CreateGuestAccountUseCase: ${e.message}")
            null
        } catch (e: HttpException) {
            Logger.debug("HTTP ${e.code()} error in CreateGuestAccountUseCase: ${e.message}")
            null
        }
    }
}
