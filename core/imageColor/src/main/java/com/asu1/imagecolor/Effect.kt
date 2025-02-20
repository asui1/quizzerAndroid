package com.asu1.imagecolor

import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.BlendModeCompat
import com.asu1.resources.R
import kotlinx.serialization.Serializable

@Serializable
enum class Effect(val stringId: Int, val resourceUrl: String, val blendmode: BlendModeCompat, val contentScale: ContentScale, val defaultEffectGraphicsInfos: List<EffectGraphicsInfo>){
    NONE(R.string.none, "", BlendModeCompat.COLOR,
        ContentScale.Fit,
        listOf()
    ),
    FIREWORKS(R.string.firework, fireworks,
        BlendModeCompat.COLOR, ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f
            )
        )
    ),
    FIREWORKS2(R.string.fireworks2, fireworks2,
        BlendModeCompat.COLOR, ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
            )
        ),
    ),
    MOON(R.string.moon, moon,
        BlendModeCompat.COLOR,
        ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
                scaleX = 0.7f,
                scaleY = 0.7f
            )
        )
    ),
    SHOOTING_STAR(R.string.shooting_star, shootingstar,
        BlendModeCompat.SRC_ATOP, ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationX = 0.2f,
                translationY = -0.2f
            ),
            EffectGraphicsInfo(
                progress = 0f,
                translationX = -0.2f,
                translationY = -0.3f
            ),
        )
    ),
    SNOWFLAKES(R.string.snow, snowflakes,
        BlendModeCompat.SRC_ATOP, ContentScale.Crop,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleX = 1.1f,
            )
        )
    ),
    CLOUDS(R.string.cloud, clouds,
        BlendModeCompat.COLOR,
        ContentScale.FillWidth,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        )
    ),
    FLOWERS(R.string.flowers, flowers,
        BlendModeCompat.COLOR,
        ContentScale.FillHeight,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        ),
    ),
    NOTES(R.string.music_notes, notes,
        BlendModeCompat.COLOR,
        ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
            )
        ),
    ),
    RAIN(R.string.rain, rain,
        BlendModeCompat.MULTIPLY,
        ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleY = 5.5f,
            )
        )
    ),
    CHRISTMAS(R.string.christmas, christmas,
        BlendModeCompat.COLOR, ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
            )
        )
    ),
    CHRISTMASBELL(R.string.christmas_bell,
        christmasbell, BlendModeCompat.COLOR,
        ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleY = 0.5f,
                scaleX = 0.5f,
                translationX = 0.25f
            ),
            EffectGraphicsInfo(
                progress = 0f,
                scaleY = 0.5f,
                scaleX = -0.5f,
                translationX = -0.25f
            ),
        )
    ),
    BUBBLES(R.string.bubbles, bubbles,
        BlendModeCompat.COLOR, ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleX = 1.5f,
                scaleY = 1.5f,
            )
        )
    ),
    HEARTS(R.string.hearts, hearts,
        BlendModeCompat.COLOR, ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            ),
            EffectGraphicsInfo(
                progress = 0.33f,
            ),
            EffectGraphicsInfo(
                progress = 0.66f,
            ),
        )
    ),
    WREATH(R.string.wreath,
        wreath, BlendModeCompat.COLOR,
        ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = 0.05f,
            )
        )
    ),
    HORIZONTAL_FLOWERS(R.string.horizontal_flowers,
        horizontalFlowers, BlendModeCompat.SRC_ATOP,
        ContentScale.FillHeight,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        )
    ),
    HOURGLASS(R.string.hourglass, hourglass,
        BlendModeCompat.COLOR, ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleY = 0.8f,
                scaleX = 0.8f,
                translationY = -0.2f
            )
        )
    ),
    COMPUTATION(R.string.computation, computation,
        BlendModeCompat.COLOR, ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f
            )
        )
    ),
    WINGS(R.string.wings, wings,
        BlendModeCompat.COLOR, ContentScale.Fit,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        )
    ),
    ;
}

