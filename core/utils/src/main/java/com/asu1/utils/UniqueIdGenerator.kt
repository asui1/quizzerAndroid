package com.asu1.utils

import java.security.MessageDigest

fun generateUniqueId(uuid: String, email: String): String {
    val concatenated = "$uuid-$email"
    // Hash the concatenated string for a uniform, unique ID
    val digest = MessageDigest.getInstance("SHA-256")
    digest.update(concatenated.toByteArray(Charsets.UTF_8))
    val hash = digest.digest()
    val hashed = hash.joinToString("") { "%02x".format(it) }

    return hashed // Or return concatenated if you skip hashing
}
