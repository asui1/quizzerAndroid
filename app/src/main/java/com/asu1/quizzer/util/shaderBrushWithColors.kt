package com.asu1.quizzer.util

import org.intellij.lang.annotations.Language

@Language("AGSL")
val basicShader = """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution;
        float minValue = distance(uv, float2(0.0, 1.0));
        return mix(color, color2, minValue);
    }
    """.trimIndent()

@Language("AGSL")
val customShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;
    
    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;
        float minValue  = distance(uv, float2(0, 1)) + abs(sin(time*0.5));
        return mix(color, color2, minValue);
    }
""".trimIndent()
