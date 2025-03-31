package com.asu1.userdatamodels

import kotlinx.serialization.Serializable

@Serializable
data class FcmToken(
    val userEmail: String,
    val fcmToken: String,
)
