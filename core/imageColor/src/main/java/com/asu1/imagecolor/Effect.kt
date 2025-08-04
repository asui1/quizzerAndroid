package com.asu1.imagecolor

import androidx.annotation.Keep
import androidx.core.graphics.BlendModeCompat
import com.asu1.resources.R
import kotlinx.serialization.Serializable

@Keep
enum class EffectTypes(val stringResource: Int){
    NONE(
        R.string.all
    ),
    CELEBRATION(
        R.string.celebration
    ),
    NATURE(
        R.string.nature
    ),
    OBJECTS(
        R.string.`object`
    ),
    EMOTION(
        R.string.emotion
    ),
    ;
}

@Keep
@Serializable
enum class Effect(
    val stringId: Int,
    val resourceUrl: String,
    val blendMode: BlendModeCompat,
    val defaultEffectGraphicsInfos: List<EffectGraphicsInfo>,
    val iconRes: String,
    val typeLabel: EffectTypes,
){
    NONE(
        R.string.none,
        "",
        BlendModeCompat.COLOR,
        listOf(),
        iconRes = "\uD83D\uDDD9",
        typeLabel = EffectTypes.NONE,
    ),
    FIREWORKS(R.string.firework, com.asu1.imagecolor.FIREWORKS,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f
            )
        ),
        iconRes = "\uD83C\uDF86",
        typeLabel = EffectTypes.CELEBRATION,
    ),
    FIREWORKS2(R.string.fireworks2, com.asu1.imagecolor.FIREWORKS2,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
            )
        ),
        iconRes = "\uD83C\uDF86",
        typeLabel = EffectTypes.CELEBRATION,
    ),
    MOON(R.string.moon, com.asu1.imagecolor.MOON,
        BlendModeCompat.MODULATE,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
                scaleX = 0.5f,
                scaleY = 0.5f
            )
        ),
        iconRes = "\uD83C\uDF19",
        typeLabel = EffectTypes.NATURE,
    ),
    SHOOTING_STAR(R.string.shooting_star, com.asu1.imagecolor.SHOOTING_STAR,
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
        ),
        iconRes = "\uD83C\uDF20",
        typeLabel = EffectTypes.NATURE,
    ),
    SNOWFLAKES(R.string.snow, com.asu1.imagecolor.SNOWFLAKES,
        BlendModeCompat.SRC_ATOP,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleX = 1.1f,
            )
        ),
        iconRes = "❄\uFE0F",
        typeLabel = EffectTypes.NATURE,
    ),
    CLOUDS(R.string.cloud, com.asu1.imagecolor.CLOUDS,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        ),
        iconRes = "☁\uFE0F",
        typeLabel = EffectTypes.NATURE,
    ),
    FLOWERS(R.string.flowers, com.asu1.imagecolor.FLOWERS,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        ),
        iconRes = "\uD83C\uDF38",
        typeLabel = EffectTypes.NATURE,
    ),
    HORIZONTAL_FLOWERS(R.string.horizontal_flowers,
        com.asu1.imagecolor.HORIZONTAL_FLOWERS, BlendModeCompat.SRC_ATOP,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f,
            )
        ),
        iconRes = "\uD83C\uDF38",
        typeLabel = EffectTypes.NATURE,
    ),
    NOTES(R.string.music_notes, com.asu1.imagecolor.NOTES,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
            )
        ),
        iconRes = "\uD83C\uDFB5",
        typeLabel = EffectTypes.OBJECTS,
    ),
    RAIN(R.string.rain, com.asu1.imagecolor.RAIN,
        BlendModeCompat.MULTIPLY,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleY = 5.5f,
            )
        ),
        iconRes = "\uD83C\uDF27\uFE0F",
        typeLabel = EffectTypes.NATURE,
    ),
    CHRISTMAS(R.string.christmas, com.asu1.imagecolor.CHRISTMAS,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f,
            )
        ),
        iconRes = "\uD83C\uDF84",
        typeLabel = EffectTypes.CELEBRATION,
    ),
    CHRISTMASBELL(R.string.bell,
        CHRISTMAS_BELL, BlendModeCompat.COLOR,
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
        ),
        iconRes = "\uD83D\uDD14",
        typeLabel = EffectTypes.OBJECTS,
    ),
    BUBBLES(R.string.bubbles, com.asu1.imagecolor.BUBBLES,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleX = 1.5f,
                scaleY = 1.5f,
            )
        ),
        iconRes = "\uD83E\uDEE7",
        typeLabel = EffectTypes.EMOTION,
    ),
    BUBBLES2(R.string.bubbles2, com.asu1.imagecolor.BUBBLES2,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        ),
        iconRes = "\uD83E\uDEE7",
        typeLabel = EffectTypes.EMOTION,
    ),
    HEARTS(R.string.hearts, com.asu1.imagecolor.HEARTS,
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
        ),
        iconRes = "\uD83D\uDC97",
        typeLabel = EffectTypes.EMOTION,
    ),
    HEARTS2(R.string.hearts2, com.asu1.imagecolor.HEARTS2,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        ),
        iconRes = "\uD83D\uDC97",
        typeLabel = EffectTypes.EMOTION,
    ),
    WREATH(R.string.wreath,
        com.asu1.imagecolor.WREATH, BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = 0.05f,
            )
        ),
        iconRes = "\uD83C\uDF3F",
        typeLabel = EffectTypes.OBJECTS,
    ),
    HOURGLASS(R.string.hourglass, com.asu1.imagecolor.HOURGLASS,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                scaleY = 0.6f,
                scaleX = 0.6f,
                translationY = -0.2f
            )
        ),
        iconRes = "⏳",
        typeLabel = EffectTypes.OBJECTS,
    ),
    COMPUTATION(R.string.computation, com.asu1.imagecolor.COMPUTATION,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.25f
            )
        ),
        iconRes = "\uD83D\uDCBB",
        typeLabel = EffectTypes.OBJECTS,
    ),
    WINGS(R.string.wings, com.asu1.imagecolor.WINGS,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.15f
            )
        ),
        iconRes = "\uD83E\uDEBD",
        typeLabel = EffectTypes.OBJECTS,
    ),
    FLOWS(R.string.flows, com.asu1.imagecolor.FLOWS,
        BlendModeCompat.SRC_ATOP,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        ),
        iconRes = "⛆",
        typeLabel = EffectTypes.EMOTION,
    ),
    FIREPILLAR(R.string.fire_pillar, FIRE_PILLAR,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        ),
        iconRes = "\uD83D\uDD25",
        typeLabel = EffectTypes.OBJECTS,
    ),
    FLOWERS2(R.string.flowers2, com.asu1.imagecolor.FLOWERS2,
        BlendModeCompat.SRC_ATOP,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        ),
        iconRes = "\uD83C\uDF38",
        typeLabel = EffectTypes.NATURE,
    ),
    TRAIN(R.string.train, com.asu1.imagecolor.TRAIN,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        ),
        iconRes = "\uD83D\uDE86",
        typeLabel = EffectTypes.OBJECTS,
    ),
    BRAINSTORMING(R.string.brainstorming, com.asu1.imagecolor.BRAINSTORMING,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        ),
        iconRes = "\uD83E\uDDE0",
        typeLabel = EffectTypes.NATURE,
    ),
    AIRBALLOON(R.string.air_balloon, AIR_BALLOON,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        ),
        iconRes = "\uD83C\uDF88",
        typeLabel = EffectTypes.OBJECTS,
    ),
    BALLOON(R.string.balloons, BALLOONS,
        BlendModeCompat.SRC_ATOP,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
            )
        ),
        iconRes = "\uD83C\uDF88",
        typeLabel = EffectTypes.OBJECTS,
    ),
    BUS(R.string.bus, com.asu1.imagecolor.BUS,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        ),
        iconRes = "\uD83D\uDE8C",
        typeLabel = EffectTypes.OBJECTS,
    ),
    TRAVEL(R.string.travel, com.asu1.imagecolor.TRAVEL,
        BlendModeCompat.COLOR,
        listOf(
            EffectGraphicsInfo(
                progress = 0f,
                translationY = -0.2f
            )
        ),
        iconRes = "\uD83E\uDDF3",
        typeLabel = EffectTypes.OBJECTS,
    ),
    ;
    companion object {
        fun fromType(type: EffectTypes): List<Effect> {
            if(type == EffectTypes.NONE) return entries
            return entries.filter { it.typeLabel == type }
        }
    }
}

