package com.asu1.activityNavigation

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.EntryPointAccessors

class FcmRetryManager(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val token = inputData.getString("fcm_token") ?: return Result.failure()

        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            FcmServiceEntryPoint::class.java
        )
        val saveFcmTokenUseCase = entryPoint.saveFcmTokenUseCase()

        val result = saveFcmTokenUseCase(token)

        return if (result.isSuccess) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}
