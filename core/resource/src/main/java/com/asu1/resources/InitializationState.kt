package com.asu1.resources

enum class InitializationState(val stringResource: Int) {
    CHECKING_FOR_UPDATES(R.string.checking_update),
    GETTING_USER_DATA(R.string.trying_login),
    DONE(R.string.empty_string),;

    fun proceed(): InitializationState {
        return entries.toTypedArray().getOrElse(ordinal + 1) { DONE }
    }
}