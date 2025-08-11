package com.asu1.network

object SecurePreferences {

        const val USERNAME = "asu1"
        var PASSWORD: String = ""
        var GOOGLE_CLIENT_ID: String = ""

        fun init(pw: String, id: String) {
                PASSWORD = pw
                GOOGLE_CLIENT_ID = id
        }
}
