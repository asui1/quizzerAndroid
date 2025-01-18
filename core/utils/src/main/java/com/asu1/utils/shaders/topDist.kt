package com.asu1.utils.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
val topDist = """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution;
        float minValue = uv.y * 0.8 + 0.1;
        return mix(color, color2, minValue);
    }
    """.trimIndent()
