package com.asu1.quizzer.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@JsonAdapter(UserInfoDeserializer::class)
data class UserInfo(
    @SerializedName("Nickname")
    val nickname: String,
    @SerializedName("Tags")
    val tags: Set<String>,
    @SerializedName("Agreed")
    val agreed: Boolean
)


data class UserRegister(
    val email: String,
    val nickname: String,
    val tags: List<String>,
    val idIcon: String
)