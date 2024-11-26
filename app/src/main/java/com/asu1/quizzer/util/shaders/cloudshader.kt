import org.intellij.lang.annotations.Language

@Language("AGSL")
val cloudShader = """
    uniform float2 resolution;
    uniform float time;
    uniform shader greyImage;
    layout(color) uniform half4 color;
    layout(color) uniform half4 color2;

    half4 main(in float2 fragCoord) {
        // Normalize fragCoord to [0, 1]
        float2 uv = fragCoord / resolution;

        // Add time-based horizontal motion
        uv.x = mod(uv.x + time * 0.1, 1.0);

        // Sample the grayscale image as the mask
        half4 greySample = greyImage.eval(uv).bgra;
        float luminance = 0.299 * greySample.r + 0.587 * greySample.g + 0.114 * greySample.b;
        // Blend the colors based on the grayscale value
        half4 blendedColor = mix(color, color2, luminance);

        return blendedColor;
    }
""".trimIndent()