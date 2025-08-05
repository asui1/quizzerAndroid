package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.UserRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class TryLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun  invoke(): UserLoginInfo? = coroutineScope {
        val userLoginInfo = userRepository.getUserLoginInfo().firstOrNull()

        if(userLoginInfo == null){
            return@coroutineScope null
        }

        val loginResult = userRepository.login(userLoginInfo.email)
        if (loginResult.isSuccessful) {

            launch {
                userRepository.saveUserLoginInfo(
                    email = userLoginInfo.email,
                    nickname = userLoginInfo.nickname,
                    urlToImage = userLoginInfo.urlToImage,
                    tags = userLoginInfo.tags ?: emptySet(),
                    agreement = userLoginInfo.agreement
                )
            }
            return@coroutineScope userLoginInfo
        }
        return@coroutineScope null
    }
}
