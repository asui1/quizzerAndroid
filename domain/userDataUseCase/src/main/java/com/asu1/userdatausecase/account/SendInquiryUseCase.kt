package com.asu1.userdatausecase.account

import com.asu1.userdata.AuthRepository
import javax.inject.Inject

class SendInquiryUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, subject: String, body: String): Result<Unit> {
        if (email.isBlank() || subject.isBlank() || body.isBlank()) {
            return Result.failure(IllegalArgumentException("Empty fields"))
        }
        return repo.sendInquiry(email.trim(), subject.trim(), body.trim())
            .recoverCatching { e ->
                throw e
            }
    }
}
