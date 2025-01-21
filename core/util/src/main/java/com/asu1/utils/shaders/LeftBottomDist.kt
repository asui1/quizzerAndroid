package com.asu1.utils.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
val leftBottomDist = """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution;
        float minValue = distance(uv, float2(0.0, 1.0)) / 1.414 * 0.8 + 0.1;
        return mix(color, color2, minValue);
    }
    """.trimIndent()
