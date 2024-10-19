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
val leftBottomShader = """
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


@Language("AGSL")
val bottomShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;
        float wave = uv.y + abs(sin(time*0.5));
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
        float wave = distance(uv, float2(0.5, 0.5)) * 2 + abs(sin(time*0.5));
        return mix(color, color2, wave);
    }
""".trimIndent()

@Language("AGSL")
val repeatShader = """
        uniform float2 resolution;
        uniform float time;
        layout(color) uniform half4 color;
        layout(color) uniform half4 color2;

        half4 main(in float2 fragCoord){
            float2 uv = fragCoord / resolution.xy;
            float wave = abs(sin(time*0.5)) * 0.9;
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
            float wave = abs(sin(time*0.5 + uv.y * 1.5708));
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
            float wave = abs(sin(time*0.5 + uv.x * 1.5708));
            return mix(color, color2, wave);
        }
""".trimIndent()