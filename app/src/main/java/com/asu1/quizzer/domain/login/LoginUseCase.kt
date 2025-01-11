package com.asu1.quizzer.domain.login

import com.asu1.quizzer.model.UserInfo
import com.asu1.quizzer.repository.UserRepository
import retrofit2.Response
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Response<UserInfo> {
        return userRepository.login(email)
    }
}