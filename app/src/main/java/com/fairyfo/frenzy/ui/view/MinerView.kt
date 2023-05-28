package com.fairyfo.frenzy.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isInvisible
import androidx.core.view.setPadding
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.ui.models.Miner
import com.fairyfo.frenzy.ui.models.MinerItem

class MinerView(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int,
) : FrameLayout(context, attrs, defStyle) {

    constructor(context: Context) : this(context, null, -1)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    private var onItemClickListener: ((MinerItem) -> Unit)? = null
    private var initialized = false

    private var previousGameState: Miner? = null
    private var currentGame: Miner? = null

    init {
        viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    initialized = true
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    currentGame?.let { setGame(it) }
                }
            },
        )
    }

    fun setGame(miner: Miner) {
        if (initialized.not()) {
            previousGameState = currentGame
            currentGame = miner
            return
        }
        removeAllViews()
        initializeView(miner)
        previousGameState = currentGame
        currentGame = miner
    }

    private fun initializeView(miner: Miner) {
        val rows = miner.matrix.size
        val columns = miner.matrix.first().size
        for (x in 0 until rows) {
            for (y in 0 until columns) {
                val defaultOffset = 60
                val cellWidth = (width - defaultOffset * 2) / rows
                val cellHeight = (height - defaultOffset * 2) / columns
                val minerItem = miner.matrix[x][y]

                val backgroundImageView = AppCompatImageView(context)
                addView(backgroundImageView)
                backgroundImageView.setImageResource(R.drawable.ic_lot)
                backgroundImageView.setPadding(12)
                backgroundImageView.layoutParams = LayoutParams(
                    cellWidth,
                    cellHeight,
                )
                backgroundImageView.x = (defaultOffset + cellWidth * x).toFloat()
                backgroundImageView.y = (defaultOffset + cellHeight * y).toFloat()

                backgroundImageView.setOnClickListener {
                    onItemClickListener?.invoke(minerItem)
                }

                val itemImageView = AppCompatImageView(context)
                addView(itemImageView)
                itemImageView.setImageResource(
                    when (minerItem.itemType) {
                        MinerItem.ItemType.Gold -> R.drawable.ic_gold
                        MinerItem.ItemType.Bomb -> R.drawable.ic_mine
                    },
                )
                val sizeMultiplier = 0.9f
                itemImageView.isInvisible = minerItem.discovered.not()
                when (minerItem.itemType) {
                    MinerItem.ItemType.Gold -> {
                        itemImageView.layoutParams = LayoutParams(
                            (cellWidth * sizeMultiplier).toInt(),
                            (cellHeight * sizeMultiplier).toInt(),
                        )
                        itemImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                        itemImageView.x =
                            defaultOffset + cellWidth * x - (cellWidth * sizeMultiplier - cellWidth) / 2f
                        itemImageView.y =
                            defaultOffset + cellHeight * y - (cellHeight * sizeMultiplier - cellHeight) / 2f
                    }

                    MinerItem.ItemType.Bomb -> {
                        itemImageView.layoutParams = LayoutParams(
                            (cellWidth * sizeMultiplier).toInt(),
                            (cellHeight * sizeMultiplier).toInt(),
                        )
                        itemImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                        itemImageView.x =
                            defaultOffset + cellWidth * x - (cellWidth * sizeMultiplier - cellWidth) / 2f
                        itemImageView.y =
                            defaultOffset + cellHeight * y - (cellHeight * sizeMultiplier - cellHeight) / 2f
                    }
                }
                itemImageView.setOnClickListener {
                    onItemClickListener?.invoke(minerItem)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (MinerItem) -> Unit) {
        onItemClickListener = listener
    }
}
