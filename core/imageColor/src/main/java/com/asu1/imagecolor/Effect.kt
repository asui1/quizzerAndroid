package com.asu1.imagecolor

import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.BlendModeCompat
import com.asu1.resources.R
import kotlinx.serialization.Serializable

@Serializable
enum class Effect(val stringId: Int, val resourceUrl: String,
                  val blendMode: BlendModeCompat,
                  val defaultEffectGraphicsInfos: List<EffectGraphicsInfo>){
    NONE(R.string.none, "", BlendModeCompat.COLOR,
        listOf()
    ),
    FIREWORKS(R.string.firework, fireworks,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f
            )
        )
    ),
    FIREWORKS2(R.string.fireworks2, fireworks2,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
            )
        ),
    ),
    MOON(R.string.moon, moon,
        BlendModeCompat.MODULATE,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
                scaleX = 0.5f,
                scaleY = 0.5f
            )
        )
    ),
    SHOOTING_STAR(R.string.shooting_star, shootingstar,
        BlendModeCompat.SRC_ATOP,
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
        BlendModeCompat.SRC_ATOP,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleX = 1.1f,
            )
        )
    ),
    CLOUDS(R.string.cloud, clouds,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        )
    ),
    FLOWERS(R.string.flowers, flowers,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        ),
    ),
    NOTES(R.string.music_notes, notes,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
            )
        ),
    ),
    RAIN(R.string.rain, rain,
        BlendModeCompat.MULTIPLY,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleY = 5.5f,
            )
        )
    ),
    CHRISTMAS(R.string.christmas, christmas,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
            )
        )
    ),
    CHRISTMASBELL(R.string.christmas_bell,
        christmasbell, BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleY = 0.5f,
                scaleX = 0.5f,
                translationX = 0.25f,
                translationY = -0.25f,
            ),
            EffectGraphicsInfo(
                progress = 0f,
                scaleY = 0.5f,
                scaleX = -0.5f,
                translationX = -0.25f,
                translationY = -0.25f,
            ),
        )
    ),
    BUBBLES(R.string.bubbles, bubbles,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleX = 1.5f,
                scaleY = 1.5f,
            )
        )
    ),
    HEARTS(R.string.hearts, hearts,
        BlendModeCompat.COLOR,
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
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = 0.05f,
            )
        )
    ),
    HORIZONTAL_FLOWERS(R.string.horizontal_flowers,
        horizontalFlowers, BlendModeCompat.SRC_ATOP,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f,
            )
        )
    ),
    HOURGLASS(R.string.hourglass, hourglass,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleY = 0.6f,
                scaleX = 0.6f,
                translationY = -0.2f
            )
        )
    ),
    COMPUTATION(R.string.computation, computation,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f
            )
        )
    ),
    WINGS(R.string.wings, wings,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.15f
            )
        )
    ),
    FLOWS(R.string.flows, flows,
        BlendModeCompat.SRC_ATOP,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        )
    ),
    FIREPILLAR(R.string.fire_pillar, firePillar,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        )
    ),
    HEARTS2(R.string.hearts2, hearts2,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        )
    ),
    FLOWERS2(R.string.flowers2, flowers2,
        BlendModeCompat.SRC_ATOP,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        )
    ),
    BUBBLES2(R.string.bubbles2, bubbles2,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        )
    ),
    TRAIN(R.string.train, train,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        )
    ),
    BRAINSTORMING(R.string.brainstorming, brainstorming,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        )
    ),
    AIRBALLOON(R.string.air_balloon, airBalloon,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        )
    ),
    BALLOON(R.string.balloons, balloons,
        BlendModeCompat.SRC_ATOP,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        )
    ),
    BUS(R.string.bus, bus,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        )
    ),
    TRAVEL(R.string.travel, travel,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        )
    ),

    ;
}

