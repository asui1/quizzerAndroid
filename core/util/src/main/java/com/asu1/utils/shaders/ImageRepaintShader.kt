package com.asu1.utils.shaders

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import org.intellij.lang.annotations.Language

@Language("AGSL")
val ImageRePainter = """
    uniform shader image;
    layout(color) uniform half4 targetColor1;
    layout(color) uniform half4 newColor1;
    layout(color) uniform half4 targetColor2;
    layout(color) uniform half4 newColor2;
    layout(color) uniform half4 targetColor3;
    layout(color) uniform half4 newColor3;
    // blendFactor controls the sensitivity of the weighting.
    uniform float blendFactor;

    half4 main(float2 fragCoord) {
        half4 origColor = image.eval(fragCoord);

        // Compute Euclidean distances in RGB space
        float d1 = distance(origColor.rgb, targetColor1.rgb);
        float d2 = distance(origColor.rgb, targetColor2.rgb);
        float d3 = distance(origColor.rgb, targetColor3.rgb);
        
        // Compute weights using an exponential decay (softmax style)
        float w1 = exp(-blendFactor * d1);
        float w2 = exp(-blendFactor * d2);
        float w3 = exp(-blendFactor * d3);
        float total = w1 + w2 + w3;
        w1 /= total;
        w2 /= total;
        w3 /= total;
        
        // Compute the resulting color as the weighted blend of the new colors.
        half3 blendedNewColor = w1 * newColor1.rgb + w2 * newColor2.rgb + w3 * newColor3.rgb;
        return half4(blendedNewColor, origColor.a);
    }
""".trimIndent()
