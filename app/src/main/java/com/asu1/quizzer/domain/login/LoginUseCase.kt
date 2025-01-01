package com.asu1.quizzer.domain.login

import com.asu1.quizzer.repository.UserRepository
import javax.inject.Inject
import retrofit2.Response
import com.asu1.quizzer.model.UserInfo

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    private fun isValidEmailOrGuest(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex) || email.contains("guest", ignoreCase = true)
    }
    suspend operator fun invoke(email: String): Response<UserInfo> {
        if (!isValidEmailOrGuest(email)) {
            throw IllegalArgumentException("Invalid email")
        }
        return userRepository.login(email)
    }
}