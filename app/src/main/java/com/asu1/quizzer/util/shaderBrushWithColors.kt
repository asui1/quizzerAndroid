package com.asu1.quizzer.util

import org.intellij.lang.annotations.Language

@Language("AGSL")
val basicShader = """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution;
        float minValue = distance(uv, float2(0.0, 1.0)) / 1.414 * 0.8 + 0.1;
        return mix(color, color2, minValue);
    }
    """.trimIndent()
// 0 ~ 1.414 -> 0.1 ~ 0.9
@Language("AGSL")
val leftBottomShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;
    
    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;
        float minValue  = (distance(uv, float2(0, 1)) + abs(sin(time*0.3))) / 2.414 * 0.8 + 0.1;
        return mix(color, color2, minValue);
    }
""".trimIndent()

// sin 0~1 distance: 0 ~ 1.414

@Language("AGSL")
val bottomShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;
        float wave = (uv.y + abs(sin(time*0.3)))/2 * 0.8 + 0.1;
        return mix(color, color2, wave);
}
""".trimIndent()

@Language("AGSL")
val centerShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;
        float wave = (distance(uv, float2(0.5, 0.5)) * 2 + abs(sin(time*0.3))) / 1.7 * 0.8 + 0.1;
        return mix(color, color2, wave);
    }
""".trimIndent()
// 0 ~ 0.7071

@Language("AGSL")
val repeatShader = """
        uniform float2 resolution;
        uniform float time;
        layout(color) uniform half4 color;
        layout(color) uniform half4 color2;

        half4 main(in float2 fragCoord){
            float2 uv = fragCoord / resolution.xy;
            float wave = abs(sin(time*0.3)) * 0.8 + 0.1;
            return mix(color, color2, wave);
        }
""".trimIndent()

@Language("AGSL")
val verticalWaveShader = """
        uniform float2 resolution;
        uniform float time;
        layout(color) uniform half4 color;
        layout(color) uniform half4 color2;

        half4 main(in float2 fragCoord){
            float2 uv = fragCoord / resolution.xy;
            float wave = abs(sin(time*0.3 + uv.y * 1.5708))  * 0.8 + 0.1;
            return mix(color, color2, wave);
        }
""".trimIndent()

@Language("AGSL")
val horizontalWaveShader = """
        uniform float2 resolution;
        uniform float time;
        layout(color) uniform half4 color;
        layout(color) uniform half4 color2;

        half4 main(in float2 fragCoord){
            float2 uv = fragCoord / resolution.xy;
            float wave = abs(sin(time*0.3 + uv.x * 1.5708))  * 0.8 + 0.1;
            return mix(color, color2, wave);
        }
""".trimIndent()