package com.asu1.utils.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
val verticalhalf = """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution;
        float y = uv.y;
        float value;
        
        if (y <= 0.4) {
            value = 0.0;
        } else if (y <= 0.6) {
            value = (y - 0.4) / 0.2;
        } else {
            value = 1.0;
        }
        
        return mix(color, color2, value);
    }
    """.trimIndent()