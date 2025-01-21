package com.asu1.imagecolor

import com.asu1.resources.R
import kotlinx.serialization.Serializable

@Serializable
enum class BackgroundBase(val resourceId: Int){
    CITY(R.drawable.empty_city_sky),
    NIGHT1(R.drawable.nightsky),
    SKY(R.drawable.sky_background),
    SNOW(R.drawable.snowbase),
    PICNIC(R.drawable.picnic2),
    CONCERT(R.drawable.emptystage),
    FLOWER(R.drawable.prettyflower),
    SUNSET(R.drawable.sunset),
    TROPHY(R.drawable.trophy),
    COAST(R.drawable.seacoast),
    NIGHT2(R.drawable.nightsky2_background),
}
