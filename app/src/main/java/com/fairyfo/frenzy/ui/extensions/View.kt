package com.fairyfo.frenzy.ui.extensions

import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.isVisible


fun View.alphaAnimator(duration: Long = 800): ViewPropertyAnimator {
    alpha = if (alpha == 1.0f) 0f else alpha
    isVisible = true
    return animate().alpha(1.0f).setDuration(duration)
}