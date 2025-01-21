package com.asu1.utils.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
val horizontalHalf = """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution;
        float x = uv.x;
        float value;
        
        if (x <= 0.4) {
            value = 0.0;
        } else if (x <= 0.6) {
            value = (x - 0.4) / 0.2;
        } else {
            value = 1.0;
        }
        
        return mix(color, color2, value);
    }
    """.trimIndent()