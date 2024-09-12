// UserRequest.kt
package com.asu1.quizzer.model

data class UserRequest(
    val email: String,
    val subject: String,
    val body: String
)