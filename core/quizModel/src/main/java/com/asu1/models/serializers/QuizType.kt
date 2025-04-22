package com.asu1.models.serializers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ShortText
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.ui.graphics.vector.ImageVector
import com.asu1.resources.R

enum class QuizType(
    val value: Int,
    val stringResourceId: Int,
    val icon: ImageVector
) {
    QUIZ1(
        0,
        R.string.multiple_choice,
        Icons.AutoMirrored.Filled.List
    ),
    QUIZ2(1,
        R.string.choose_date,
        Icons.Default.CalendarToday
    ),
    QUIZ3(2,
        R.string.reorder_item,
        Icons.Default.SwapVert
    ),
    QUIZ4(3,
        R.string.connect_items,
        Icons.Default.Link
    ),
    QUIZ5(
        4,
        R.string.short_answer,
        Icons.AutoMirrored.Filled.ShortText
    ),
    QUIZ6(
        5,
        R.string.fill_blank,
        Icons.Default.TextFields
    ),
}
