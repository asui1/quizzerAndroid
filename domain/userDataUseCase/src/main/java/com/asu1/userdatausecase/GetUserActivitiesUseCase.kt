package com.asu1.userdatausecase

import com.asu1.userdata.UserRepository
import com.asu1.userdatamodels.UserActivity
import com.asu1.utils.Logger
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserActivitiesUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Result<List<UserActivity>> {
        // 1) Parameter check with IllegalArgumentException
        if (email.isEmpty()) {
            Logger.debug("GetUserActivitiesUseCase: email is empty")
            return Result.failure(IllegalArgumentException("Email must not be empty"))
        }

        return try {
            userRepository.getUserActivities(email)
        } catch (e: IOException) {
            Logger.debug("Network error fetching activities for $email: ${e.message}")
            Result.failure(e)
        } catch (e: HttpException) {
            Logger.debug("HTTP ${e.code()} error fetching activities for $email")
            Result.failure(e)
        }
    }
}
