package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import kotlinx.coroutines.coroutineScope
import java.util.logging.Logger
import javax.inject.Inject

class InitLoginUseCase @Inject constructor(
    private val tryLoginUseCase: TryLoginUseCase,
    private val tryGuestLoginUseCase: TryGuestLoginUseCase,
    private val createGuestAccountUseCase: CreateGuestAccountUseCase
) {
    suspend operator fun  invoke(isKo: Boolean): UserLoginInfo? = coroutineScope {

        tryLoginUseCase().run{
            if (this != null) {
                return@coroutineScope this
            }
        }
        Logger.getLogger("InitLoginUseCase").info("tryLoginUseCase failed")

        tryGuestLoginUseCase().run{
            if (this != null) {
                return@coroutineScope this
            }
        }
        Logger.getLogger("InitLoginUseCase").info("tryGuestLoginUseCase failed")
        createGuestAccountUseCase(isKo).run {
            if (this != null) {
                return@coroutineScope this
            }
        }
        Logger.getLogger("InitLoginUseCase").info("createGuestAccountUseCase failed")
        return@coroutineScope null
    }
}