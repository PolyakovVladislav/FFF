package com.fairyfo.frenzy.ui.fragments.bonusGame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fairyfo.frenzy.ui.models.Miner
import com.fairyfo.frenzy.ui.models.MinerItem

class BonusGameViewModel : ViewModel() {

    companion object {
        const val rows = 4
        const val columns = 4
        const val bombsCount = 3
        const val multiplier = 0.13f
    }

    private val _balance = MutableLiveData<Long>()
    val balance: LiveData<Long> = _balance

    private val _win = MutableLiveData<Long>()
    val win: LiveData<Long> = _win

    private val _game = MutableLiveData<Miner>()
    val game: LiveData<Miner> = _game

    private var currentBet = 0L

    init {
        generateMatrix()
    }

    fun play(bet: Long, balance: Long) {
        val collected = if (_game.value?.gameState == Miner.GameState.Finished) {
            0
        } else {
            getCollected(currentBet)
        }
        _win.value = collected
        generateMatrix()
        _balance.value = balance - bet + collected
        currentBet = bet
    }

    private fun getCollected(bet: Long): Long {
        var currentMultiplier = 1f
        _game.value?.matrix?.forEach { minerItems ->
            val discoveredItemsCount = minerItems.count { item ->
                item.discovered && item.itemType == MinerItem.ItemType.Gold
            }
            currentMultiplier += discoveredItemsCount * multiplier
        }
        return (bet * currentMultiplier).toLong()
    }

    fun onItemClicked(minerItem: MinerItem) {
        if (_game.value?.gameState == Miner.GameState.Finished) {
            return
        }
        val miner = _game.value
        miner?.matrix?.forEach { row ->
            row.forEach { item ->
                item.animateDiscover = false
            }
        }
        when (minerItem.itemType) {
            MinerItem.ItemType.Gold -> {
                if (minerItem.discovered.not()) {
                    _game.value = miner?.apply {
                        matrix[minerItem.x][minerItem.y].discovered = true
                        matrix[minerItem.x][minerItem.y].animateDiscover = true
                    }
                }
            }

            MinerItem.ItemType.Bomb -> {
                if (minerItem.discovered.not()) {
                    _game.value = miner?.apply {
                        matrix[minerItem.x][minerItem.y].discovered = true
                        matrix[minerItem.x][minerItem.y].animateDiscover = true
                        gameState = Miner.GameState.Finished
                    }
                    _win.value = 0
                }
            }
        }
    }

    private fun generateMatrix() {
        val bombsPositions = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until bombsCount) {
            var x: Int
            var y: Int
            do {
                x = (0 until rows).random()
                y = (0 until columns).random()
            } while (bombsPositions.any { it.first != x && it.second != y })
            bombsPositions.add(x to y)
        }
        val matrix = Array(rows) { i ->
            Array(columns) { j ->
                if (bombsPositions.any { it.first == i && it.second == j }) {
                    MinerItem(false, false, MinerItem.ItemType.Bomb, i, j)
                } else {
                    MinerItem(false, false, MinerItem.ItemType.Gold, i, j)
                }
            }
        }
        _game.value = Miner(matrix, Miner.GameState.Playing)
    }
}
