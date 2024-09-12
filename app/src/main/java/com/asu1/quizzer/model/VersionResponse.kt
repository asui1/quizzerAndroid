package com.asu1.quizzer.model

import com.google.gson.annotations.SerializedName

data class VersionResponse(
    @SerializedName("latestVersion")
    val latestVersion: String
)
