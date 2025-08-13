package com.asu1.userdatausecase.account

import com.asu1.userdata.AuthRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> =
        repo.deleteUser(email)
}
