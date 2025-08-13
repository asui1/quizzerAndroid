package com.asu1.userdatausecase.user

import com.asu1.userdata.ActivityRepository
import com.asu1.userdatamodels.UserActivity
import com.asu1.utils.Logger
import javax.inject.Inject

class GetUserActivitiesUseCase @Inject constructor(
    private val activityRepository: ActivityRepository
) {
    suspend operator fun invoke(email: String): Result<List<UserActivity>> {
        val normalized = email.trim()
        if (normalized.isEmpty()) {
            Logger.debug("GetUserActivitiesUseCase: email is empty")
            return Result.failure(IllegalArgumentException("Email must not be empty"))
        }
        return activityRepository.getUserActivities(normalized)
    }
}