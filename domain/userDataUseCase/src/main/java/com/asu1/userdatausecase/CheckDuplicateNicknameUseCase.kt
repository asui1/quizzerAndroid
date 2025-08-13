package com.asu1.userdatausecase

import com.asu1.userdata.AuthRepository
import com.asu1.userdatamodels.UserRegister
import javax.inject.Inject

class CheckDuplicateNicknameUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(nickname: String): Result<Unit> =
        repo.checkDuplicateNickname(nickname)
}

class RegisterUserUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(req: UserRegister): Result<Unit> =
        repo.register(req)
}
