package com.asu1.quizzer.domain.login

import com.asu1.quizzer.repository.UserRepository
import javax.inject.Inject
import retrofit2.Response
import com.asu1.quizzer.model.UserInfo

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Response<UserInfo> {
        return userRepository.login(email)
    }
}