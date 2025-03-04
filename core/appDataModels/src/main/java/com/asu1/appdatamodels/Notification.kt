package com.asu1.appdatamodels

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val title: String,
    val date: String,
    val id: Int,
    val body: String = "",
)

val sampleNotification = Notification(
    title = "Notification Example",
    date = "2025.02.27",
    id = 1,
    body = "This is example notification. This will have lots of text to represent it as a notification. This is example notification. This will have lots of text to represent it as a notification. This is example notification. This will have lots of text to represent it as a notification. This is example notification. This will have lots of text to represent it as a notification.",
)

val sampleNotificationList = listOf(
    sampleNotification,
    sampleNotification.copy(title = "Notification2 Example", id = 2),
    sampleNotification.copy(title = "Notification3 Example", id = 3),
    sampleNotification.copy(title = "Notification4 Example", id = 4),
    sampleNotification.copy(title = "Notification5 Example", id = 5),
)