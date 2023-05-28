package com.fairyfo.frenzy.ui.extensions

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.text.TextPaint


fun TextPaint.setTextGradient(x0: Float, y0: Float, x1: Float, y1: Float, colors: IntArray) {
    val textShader: Shader = LinearGradient(
        x0, y0, x1, y1, colors, null, TileMode.CLAMP
    )
    shader = textShader
}