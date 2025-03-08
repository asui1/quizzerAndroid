package com.asu1.utils.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
val horizontalHalf = """
    uniform float2 resolution;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    // Simple noise function
    float random(float2 p) {
        return fract(sin(dot(p, float2(12.9898, 78.233))) * 43758.5453);
    }

    half4 main(in float2 fragCoord) {
        float2 uv = fragCoord / resolution;
        float x = uv.x;

        // Generate a smooth noise value based on pixel position
        float noise = (random(fragCoord * 0.05) - 0.5) * 0.1; // Subtle noise variation

        // Smooth transition from 0.3 to 0.7 with slight noise
        float value = smoothstep(0.3, 0.7, x + noise);

        return mix(color, color2, value);
    }
    """.trimIndent()
