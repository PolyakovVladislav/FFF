package com.fairyfo.frenzy.ui.extensions

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun TextView.setTextGradient(@ColorRes vararg colors: Int) {
    paint.setTextGradient(
        0f,
        0f,
        width.toFloat(),
        textSize + paddingTop,
        colors.map { ContextCompat.getColor(context, it) }.toIntArray()
    )
}