package com.asu1.userdatamodels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserInfo(
    @SerialName("Nickname") val nickname: String,
    @SerialName("Tags")     val tags: Set<String>, // or List<String> if order/dupes matter
    @SerialName("Agreed")   val agreed: Boolean
)

@Serializable
data class GuestAccount(
    val email: String,
    val nickname: String,
)

data class UserRegister(
    val email: String,
    val nickname: String,
    val tags: List<String>,
    val idIcon: String
)
