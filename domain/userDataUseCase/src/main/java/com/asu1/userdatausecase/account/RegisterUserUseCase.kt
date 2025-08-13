package com.asu1.userdatausecase.account

import com.asu1.userdata.AuthRepository
import com.asu1.userdatamodels.UserRegister
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(req: UserRegister): Result<Unit> =
        repo.register(req)
}
