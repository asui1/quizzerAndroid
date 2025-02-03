package com.asu1.resources

import kotlinx.serialization.Serializable

@Serializable
data class SearchBase(
    val text: String,
    val category: String
)
