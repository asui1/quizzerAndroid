package com.asu1.appdatamodels

import kotlinx.serialization.Serializable
import com.asu1.resources.R
import kotlinx.serialization.Transient

@Serializable
data class Notification(
    val title: String,
    val date: String,
    val id: Int,
    val body: String = "",
){
    companion object{
        val update: List<String> = listOf(
            "업데이트", "Update"
        )
        val notice: List<String> = listOf(
            "Notification", "안내"
        )
    }
    @Transient
    val tag: Int = when {
        update.any { title.contains(it, ignoreCase = true) } -> R.string.update
        notice.any { title.contains(it, ignoreCase = true) } -> R.string.notification
        else -> R.string.empty_string
    }

    @Transient
    val cleanTitle: String = tag.let {
        val keyword = (update + notice).firstOrNull { title.contains(it, ignoreCase = true) }
        keyword?.let { title.replace(it, "", ignoreCase = true).trim() } ?: title
    }
}

val sampleNotification = Notification(
    title = "1.0.11 Notification",
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