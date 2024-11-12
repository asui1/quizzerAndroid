package com.asu1.quizzer.util

import java.security.MessageDigest
import java.util.logging.Logger

fun generateUniqueId(uuid: String, email: String): String {
    val concatenated = "$uuid-$email"

    // Hash the concatenated string for a uniform, unique ID
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(concatenated.toByteArray())
    val hashed = hash.joinToString("") { "%02x".format(it) }

    Logger.getLogger("UniqueIdGenerator").info("GEN UNIQUE ID $hashed")

    return hashed // Or return concatenated if you skip hashing
}
