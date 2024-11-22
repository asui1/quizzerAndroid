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

@Language("AGSL")
val leftBottomShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    // Simple hash function for pseudo-randomness
    float hash(float n) {
        return fract(sin(n) * 43758.5453123);
    }

    // 2D noise function
    float noise(float2 p) {
        float2 i = floor(p);
        float2 f = fract(p);
        float a = hash(i.x + i.y * 57.0);
        float b = hash(i.x + 1.0 + i.y * 57.0);
        float c = hash(i.x + (i.y + 1.0) * 57.0);
        float d = hash(i.x + 1.0 + (i.y + 1.0) * 57.0);
        float2 u = f * f * (3.0 - 2.0 * f);
        return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
    }

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;

        // Distance from the left-bottom corner
        float dist = distance(uv, float2(0.0, 1.0));

        // Add time-based variation and noise
        float t = time * 0.3;
        float noiseVal = noise(uv * 10.0 + t) * 0.5 + 0.5; // Scale noise to [0, 1]
        float wave = sin(dist * 10.0 + time) * 0.5 + 0.5;

        // Combine distance, noise, and wave effects
        float minValue = (dist + abs(sin(t)) + wave * noiseVal) / 2.414 * 0.8 + 0.1;

        // Mix colors based on the calculated value
        half4 finalColor = mix(color, color2, minValue);

        return finalColor;
    }
""".trimIndent()

// sin 0~1 distance: 0 ~ 1.414

@Language("AGSL")
val bottomShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    // Simple hash function for pseudo-randomness
    float hash(float n) {
        return fract(sin(n) * 43758.5453123);
    }

    // 2D noise function
    float noise(float2 p) {
        float2 i = floor(p);
        float2 f = fract(p);
        float a = hash(i.x + i.y * 57.0);
        float b = hash(i.x + 1.0 + i.y * 57.0);
        float c = hash(i.x + (i.y + 1.0) * 57.0);
        float d = hash(i.x + 1.0 + (i.y + 1.0) * 57.0);
        float2 u = f * f * (3.0 - 2.0 * f);
        return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
    }

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;

        // Generate noise to influence the sin function
        float noiseVal = noise(uv * 10.0 + time) * 0.5 + 0.5;

        // Modulate the sin function with noise for dynamic wave pattern
        float wave = (uv.y + abs(sin((time * 0.3) + noiseVal * 3.0))) / 2.0 * 0.8 + 0.1;

        // Mix colors based on the calculated wave value
        half4 finalColor = mix(color, color2, wave);

        return finalColor;
    }
""".trimIndent()


@Language("AGSL")
val centerShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    // Simple hash function for pseudo-randomness
    float hash(float n) {
        return fract(sin(n) * 43758.5453123);
    }

    // 2D noise function
    float noise(float2 p) {
        float2 i = floor(p);
        float2 f = fract(p);
        float a = hash(i.x + i.y * 57.0);
        float b = hash(i.x + 1.0 + i.y * 57.0);
        float c = hash(i.x + (i.y + 1.0) * 57.0);
        float d = hash(i.x + 1.0 + (i.y + 1.0) * 57.0);
        float2 u = f * f * (3.0 - 2.0 * f);
        return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
    }

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;

        // Calculate distance from the center
        float dist = distance(uv, float2(0.5, 0.5)) * 2.0;

        // Generate noise to influence the sin function
        float noiseVal = noise(uv * 10.0 + time) * 0.5 + 0.5;

        // Modulate the sin function with noise for dynamic wave pattern
        float wave = (dist + abs(sin((time * 0.3) + noiseVal * 3.0))) / 1.7 * 0.8 + 0.1;

        // Mix colors based on the calculated wave value
        half4 finalColor = mix(color, color2, wave);

        return finalColor;
    }
""".trimIndent()

// 0 ~ 0.7071

@Language("AGSL")
val repeatShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    // Simple hash function for pseudo-randomness
    float hash(float n) {
        return fract(sin(n) * 43758.5453123);
    }

    // 2D noise function
    float noise(float2 p) {
        float2 i = floor(p);
        float2 f = fract(p);
        float a = hash(i.x + i.y * 57.0);
        float b = hash(i.x + 1.0 + i.y * 57.0);
        float c = hash(i.x + (i.y + 1.0) * 57.0);
        float d = hash(i.x + 1.0 + (i.y + 1.0) * 57.0);
        float2 u = f * f * (3.0 - 2.0 * f);
        return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
    }

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;

        // Generate noise to influence the sin function
        float noiseVal = noise(uv * 10.0 + time) * 0.5 + 0.5;

        // Modulate the sin function with noise for a dynamic wave effect
        float wave = abs(sin((time * 0.3) + noiseVal * 3.0)) * 0.8 + 0.1;

        // Mix colors based on the calculated wave value
        half4 finalColor = mix(color, color2, wave);

        return finalColor;
    }
""".trimIndent()


@Language("AGSL")
val verticalWaveShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    // Simple hash function for pseudo-randomness
    float hash(float n) {
        return fract(sin(n) * 43758.5453123);
    }

    // 2D noise function
    float noise(float2 p) {
        float2 i = floor(p);
        float2 f = fract(p);
        float a = hash(i.x + i.y * 57.0);
        float b = hash(i.x + 1.0 + i.y * 57.0);
        float c = hash(i.x + (i.y + 1.0) * 57.0);
        float d = hash(i.x + 1.0 + (i.y + 1.0) * 57.0);
        float2 u = f * f * (3.0 - 2.0 * f);
        return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
    }

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;

        // Generate noise to influence the sin function
        float noiseVal = noise(uv * 10.0 + time) * 0.5 + 0.5;

        // Modulate the sin function with noise for dynamic vertical wave
        float wave = abs(sin(-time * 0.3 + uv.y * 1.5708 + noiseVal * 3.0)) * 0.8 + 0.1;

        // Mix colors based on the calculated wave value
        half4 finalColor = mix(color, color2, wave);

        return finalColor;
    }
""".trimIndent()

@Language("AGSL")
val horizontalWaveShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    // Simple hash function for pseudo-randomness
    float hash(float n) {
        return fract(sin(n) * 43758.5453123);
    }

    // 2D noise function
    float noise(float2 p) {
        float2 i = floor(p);
        float2 f = fract(p);
        float a = hash(i.x + i.y * 57.0);
        float b = hash(i.x + 1.0 + i.y * 57.0);
        float c = hash(i.x + (i.y + 1.0) * 57.0);
        float d = hash(i.x + 1.0 + (i.y + 1.0) * 57.0);
        float2 u = f * f * (3.0 - 2.0 * f);
        return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
    }

    half4 main(in float2 fragCoord){
        float2 uv = fragCoord / resolution.xy;

        // Generate noise to influence the sin function
        float noiseVal = noise(uv * 10.0 + time) * 0.5 + 0.5;

        // Modulate the sin function with noise for dynamic horizontal wave
        float wave = abs(sin(-time * 0.3 + uv.x * 1.5708 + noiseVal * 3.0)) * 0.8 + 0.1;

        // Mix colors based on the calculated wave value
        half4 finalColor = mix(color, color2, wave);

        return finalColor;
    }
""".trimIndent()

