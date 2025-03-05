package com.asu1.utils.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
val topDist = """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution;
        
        // Define the y-wave function
        float yWave = 0.4 + sin(uv.x * 3.14159265359) * 0.2;
        
        // Compute minValue based on varying slope
        float minValue;
        if (uv.y < yWave) {
            minValue = ((uv.y - yWave) * 0.8 / yWave) + 0.5;
        } else {
            minValue = ((uv.y - yWave) * 0.8 / (1.0 - yWave)) + 0.5;
        }
        
        return mix(color, color2, minValue);
    }
        """.trimIndent()
