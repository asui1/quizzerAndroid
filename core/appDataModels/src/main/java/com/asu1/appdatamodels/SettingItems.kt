package com.asu1.appdatamodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.ui.graphics.vector.ImageVector
import com.asu1.resources.R

data class SettingItems(
    val stringResourceId: Int,
    val vectorIcon: ImageVector,
    val onClick: () -> Unit,
)

val sampleSettingItems = SettingItems(
    stringResourceId =  R.string.settings,
    vectorIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
    onClick = {},
)