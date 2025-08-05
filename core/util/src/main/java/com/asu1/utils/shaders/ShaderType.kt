package com.asu1.utils.shaders

import com.asu1.resources.R

enum class ShaderType(val shaderName: Int, val index: Int, val agslShader: String) {
    Brush1(R.string.left_bottom, 0, leftBottomDist),
    Brush2(R.string.left, 1, leftDist),
    Brush3(R.string.top, 2, topDist),
    Brush4(R.string.diagonal, 3, diagonalHalf),
    Brush5(R.string.horizontal, 4, horizontalHalf),
    Brush6(R.string.vertical, 5, verticalhalf),
    Brush7(R.string.top_down, 6, verticalRepeat)

    ;
}
