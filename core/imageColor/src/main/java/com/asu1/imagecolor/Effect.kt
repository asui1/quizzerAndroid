package com.asu1.imagecolor

import com.asu1.resources.R
import kotlinx.serialization.Serializable

@Serializable
enum class Effect(val stringId: Int){
    NONE(R.string.none),
    FIREWORKS(R.string.firework),
    FIREWORKS2(R.string.fireworks2),
    MOON(R.string.moon),
    SHOOTING_STAR(R.string.shooting_star),
    SNOWFLAKES(R.string.snow),
    CLOUDS(R.string.cloud),
    FLOWERS(R.string.flowers),
    NOTES(R.string.music_notes),
    RAIN(R.string.rain),
    CHRISTMAS(R.string.christmas),
    CHRISTMASBELL(R.string.christmas_bell),
    BUBBLES(R.string.bubbles),
    HEARTS(R.string.hearts),
    WREATH(R.string.wreath);
}