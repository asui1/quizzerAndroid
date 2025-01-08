package com.asu1.quizzer.domain.login

import com.asu1.quizzer.repository.UserRepository
import javax.inject.Inject

class CreateGuestAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute(isKo: Boolean) {
        userRepository.guestAccount(isKo)
    }
}