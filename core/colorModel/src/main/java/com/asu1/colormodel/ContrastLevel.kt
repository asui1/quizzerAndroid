package com.asu1.colormodel

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.asu1.resources.R
import com.materialkolor.Contrast

@Keep
enum class ContrastLevel(
    @param:StringRes val stringResource: Int,
    val contrast: Contrast
){
    Default(
        stringResource = R.string.contrast_default,
        contrast = Contrast.Default
    ),
    Medium(
        stringResource = R.string.contrast_medium,
        contrast = Contrast.Medium
    ),
    High(
        stringResource = R.string.contrast_high,
        contrast = Contrast.High
    ),
    Reduced(
        stringResource = R.string.contrast_reduced,
        contrast = Contrast.Reduced
    ),
}

