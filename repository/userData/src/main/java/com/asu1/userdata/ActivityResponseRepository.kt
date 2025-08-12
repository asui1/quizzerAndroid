package com.asu1.userdata

import com.asu1.network.ActivityApi
import com.asu1.network.runApi
import com.asu1.userdatamodels.UserActivity
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

interface ActivityRepository {
    suspend fun getUserActivities(email: String): Result<List<UserActivity>>
}

@Singleton
class ActivityRepositoryImpl @Inject constructor(
    private val activityApi: ActivityApi
) : ActivityRepository {

    private val cache = mutableMapOf<String, List<UserActivity>>() // email -> activities

    override suspend fun getUserActivities(email: String): Result<List<UserActivity>> {
        cache[email]?.let { return Result.success(it) }

        val res = runApi { activityApi.getUserActivity(email) }
            .mapCatching { resp ->
                if (!resp.isSuccessful) throw HttpException(resp)
                resp.body() ?: throw NoSuchElementException("Empty body")
            }

        res.onSuccess { cache[email] = it }
        return res
    }
}
