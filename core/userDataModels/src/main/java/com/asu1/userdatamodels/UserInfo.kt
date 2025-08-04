package com.asu1.userdatamodels

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@JsonAdapter(UserInfoDeserializer::class)
data class UserInfo(
    @SerializedName("Nickname")
    val nickname: String,
    @SerializedName("Tags")
    val tags: Set<String>,
    @SerializedName("Agreed")
    val agreed: Boolean
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
