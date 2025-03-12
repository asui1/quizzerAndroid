package com.asu1.imagecolor

import androidx.compose.ui.graphics.BlendMode
import com.asu1.resources.R
import kotlinx.serialization.Serializable

@Serializable
enum class BackgroundBase(val resourceId: Int){
    CITY(R.drawable.empty_city_sky),
    NIGHT1(R.drawable.nightsky),
    NIGHT2(R.drawable.nightsky2_background),
    SKY_WARM(R.drawable.sky_warm),
    SKY_COOL(R.drawable.sky_cool),
    SKY_MYSTIC(R.drawable.sky_mystic),
    FIELD(R.drawable.field),
    SUNSET_FIELD(R.drawable.sunset_field),
    SUNSET(R.drawable.sunset),
    CONCERT(R.drawable.concert),
    CONCERT_DIMMER(R.drawable.concert_less),
    FLOWER(R.drawable.prettyflower),
    TROPHY(R.drawable.trophy),
    COAST(R.drawable.seacoast),
    GLOWING_TROPHY(R.drawable.glowing_trophy),
    SNOW(R.drawable.snowbase),
    SNOW_WITH_FENCE(R.drawable.snow_with_fence),
    SNOW_WITH_LAKE(R.drawable.snow_with_lake),
    SNOW_WITH_PEACE(R.drawable.snow_peaceful),
    ROOM_COZY(R.drawable.room_cozy),
    ROOM_COZY_MODERN(R.drawable.room_cozy_modern),
    ROOM_FUTURE(R.drawable.room_future),
    ROOM_HOLOGRAPHIC(R.drawable.room_holographic),
    ROOM_VIRTUAL(R.drawable.room_virtual),
    ROOM_VIRTUAL2(R.drawable.room_virtual2),
    ROOM_VIRTUAL_STREAM(R.drawable.room_virtual_stream),
    LAB_FUTURE(R.drawable.lab_future),
    LAB_COZY(R.drawable.lab_cozy),
    LAB_GAMELIKE(R.drawable.lab_gamelike),
    CITY_BREATHTAKING(R.drawable.city_breathtaking),
    CITY_CYBERPUNK(R.drawable.city_cyberpunk),
    CITY_NEON(R.drawable.city_neon),
    CITY_PASTEL(R.drawable.city_pastel),
    CITY_CHERRYBLOSSOM(R.drawable.city_cherryblossom),
}

@Serializable
enum class ImageBlendMode(val blendMode: BlendMode, val stringResourceId: Int){
    BLENDCOLOR(BlendMode.Color, R.string.blend_color),
    BLENDHUE(BlendMode.Hue, R.string.blend_hue)
}

//TODO: ADD IMAGE TYPE FILTERS.
