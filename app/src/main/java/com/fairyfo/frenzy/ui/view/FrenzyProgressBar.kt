package com.fairyfo.frenzy.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.children
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.databinding.ViewFrenzyProgressBarBinding


class FrenzyProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): FrameLayout(context, attrs, defStyle) {

    private val binding by lazy {
        ViewFrenzyProgressBarBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private var changeListener: ((Int) -> Unit)? = null

    var clicksEnabled = true

    init {
        binding.root.children.forEachIndexed { i, view ->
            (view as AppCompatImageView).setOnClickListener {
                if (clicksEnabled.not()) {
                    return@setOnClickListener
                }
                progress = when (i) {
                    0 -> {
                        0
                    }
                    binding.root.children.count() -> {
                        100
                    }
                    else -> {
                        (100f / 7 * i).toInt()
                    }
                }
            }
        }
    }

    var progress: Int = 0
        set(value) {
            field = value
            updateProgress()
            changeListener?.let { it(value) }
        }

    fun setOnChangeListener(listener: (Int) -> Unit) {
        changeListener = listener
    }

    private fun updateProgress() {
        val internalProgress = 8f * progress / 100
        binding.root.children.forEachIndexed { i, view ->
            (view as AppCompatImageView).apply {
                setImageResource(R.drawable.ic_progress_item)
                alpha = if (i >= internalProgress) {
                    0.4f
                } else {
                    1f
                }
            }
        }
    }
}
