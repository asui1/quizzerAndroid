package com.asu1.userdatausecase

import com.asu1.userdata.UserRepository
import com.asu1.userdatamodels.UserActivity
import com.asu1.utils.Logger
import javax.inject.Inject

class GetUserActivitiesUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun  invoke(email: String): Result<List<UserActivity>> {
        if(email.isEmpty()) {
            return Result.failure(Exception("Email is empty"))
        }
        return try {
            userRepository.getUserActivities(email)
        } catch (e: Exception) {
            Logger.debug("Error in GetUserActivitiesUseCase: $e")
            Result.failure(e)
        }
    }
}