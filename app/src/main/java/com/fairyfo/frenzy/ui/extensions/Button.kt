package com.fairyfo.frenzy.ui.extensions

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

fun MaterialButton.setTextGradient(@ColorRes vararg colors: Int) {
    paint.setTextGradient(
        0f,
        0f,
        width.toFloat(),
        textSize + paddingTop,
        colors.map { ContextCompat.getColor(context, it) }.toIntArray()
    )
}