package com.asu1.userdatausecase.account

import com.asu1.userdata.AuthRepository
import javax.inject.Inject

class CheckDuplicateNicknameUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(nickname: String): Result<Unit> =
        repo.checkDuplicateNickname(nickname)
}
