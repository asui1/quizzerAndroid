package com.asu1.colormodel

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.materialkolor.PaletteStyle
import com.asu1.resources.R

@Keep
enum class PaletteLevel(
    @param:StringRes val stringResource: Int,
    val palette: PaletteStyle?
){
    TonalSpot(
        stringResource = R.string.palette_tonal,
        palette = PaletteStyle.TonalSpot,
    ),
    Neutral(
        stringResource = R.string.palette_neutral,
        palette = PaletteStyle.Neutral,
    ),
    Vibrant(
        stringResource = R.string.palette_vibrant,
        palette = PaletteStyle.Vibrant,
    ),
    Expressive(
        stringResource = R.string.palette_expressive,
        palette = PaletteStyle.Expressive,
    ),
    Rainbow(
        stringResource = R.string.palette_rainbow,
        palette = PaletteStyle.Rainbow,
    ),
    FruitSalad(
        stringResource = R.string.palette_fruit,
        palette = PaletteStyle.FruitSalad,
    ),
    Fidelity(
        stringResource = R.string.palette_fidel,
        palette = PaletteStyle.Fidelity,
    ),
    Content(
        stringResource = R.string.palette_content,
        palette = PaletteStyle.Content,
    ),
    Strict(
        stringResource = R.string.strict,
        palette = null,
    ),
}
