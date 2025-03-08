package com.asu1.userdatausecase

import com.asu1.resources.UserLoginInfo
import com.asu1.userdata.UserRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginWithEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun  invoke(email: String, profileUri: String): UserLoginInfo? = coroutineScope {
        if(email.isEmpty()){
            return@coroutineScope null
        }
        val loginResult = userRepository.login(email)
        if (!loginResult.isSuccessful) return@coroutineScope null
        val userLoginInfo = loginResult.body() ?: return@coroutineScope null

        launch {
            userRepository.saveUserLoginInfo(
                email = email,
                nickname = userLoginInfo.nickname,
                urlToImage = profileUri,
                tags = userLoginInfo.tags,
                agreement = false
            )
        }

        return@coroutineScope UserLoginInfo(
            email = email,
            nickname = userLoginInfo.nickname,
            urlToImage = profileUri,
            tags = userLoginInfo.tags,
            agreement = false
        )
    }
}