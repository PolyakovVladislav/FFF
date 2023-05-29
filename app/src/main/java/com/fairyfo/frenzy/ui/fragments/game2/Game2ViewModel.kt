package com.fairyfo.frenzy.ui.fragments.game2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fairyfo.frenzy.R
import com.fairyfo.frenzy.ui.models.Slot
import com.fairyfo.frenzy.utils.SharedPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow


class Game2ViewModel : ViewModel() {

    companion object {
        private const val FREQUENCY = 20L
        private val values = listOf(
            R.drawable.ic_lemon2,
            R.drawable.ic_tomato,
            R.drawable.ic_plum2,
            R.drawable.ic_watermalon2,
            R.drawable.ic_grape2,
        )
        private val combinations = mutableListOf<IntArray>().apply {
            for (i in 0..5) {
                add(intArrayOf(values[0], values[0], values[0]))
                add(intArrayOf(values[4], values[4], values[4]))
            }
            for (i in 0..3) {
                add(intArrayOf(values[1], values[1], values[1]))
                add(intArrayOf(values[3], values[3], values[3]))
            }
            for (i in 0..1) {
                add(intArrayOf(values[2], values[2], values[2]))
            }
            for (i in 0..100) {
                add(intArrayOf(values.random(), values.random(), values.random()))
            }
        }
    }

    private val _slot1LiveData = MutableLiveData<List<Slot>>()
    val slot1LiveData: LiveData<List<Slot>> = _slot1LiveData

    private val _slot2LiveData = MutableLiveData<List<Slot>>()
    val slot2LiveData: LiveData<List<Slot>> = _slot2LiveData

    private val _slot3LiveData = MutableLiveData<List<Slot>>()
    val slot3LiveData: LiveData<List<Slot>> = _slot3LiveData

    private val _balance = MutableLiveData<Long>()
    val balance: LiveData<Long> = _balance

    private val _win = MutableLiveData<Long>()
    val win: LiveData<Long> = _win

    private var gameState = GameState.Idle

    init {
        generateSlots()
    }

    fun play(bet: Long, prefs: SharedPrefs) {
        viewModelScope.launch {
            if (gameState == GameState.Idle) {
                generateSlots()
                _balance.value = prefs.balance - bet

                gameState = GameState.Rolling
                launch { rollSlot(_slot1LiveData) }
                delay(300)
                launch { rollSlot(_slot2LiveData) }
                delay(300)
                rollSlot(_slot3LiveData)

                val multiplier = getMultiplier()
                if (multiplier == 0f) {
                    if (prefs.balance == 0L) {
                        _balance.value = 5000L
                    }
                } else {
                    _win.value = (multiplier * bet).toLong()
                    _balance.value = prefs.balance + (multiplier * bet + bet).toLong()
                }

                gameState = GameState.Idle
            }
        }
    }

    private fun getMultiplier(): Float {
        val slot1 = _slot1LiveData.value?.let { slots ->
            slots[slots.size - 2].drawableRes
        }
        val slot2 = _slot2LiveData.value?.let { slots ->
            slots[slots.size - 2].drawableRes
        }
        val slot3 = _slot3LiveData.value?.let { slots ->
            slots[slots.size - 2].drawableRes
        }
        val combination = listOf(
            slot1 ?: return 0f,
            slot2 ?: return 0f,
            slot3 ?: return 0f,
        )

        if (combination.distinct().size == 1) { // Комбинация из 1го элемента
            return when (combination.first()) {
                values[0] -> 3f
                values[1] -> 6f
                values[2] -> 9f
                values[3] -> 6f
                values[4] -> 3f
                else -> 0f
            }
        }

        // Проверяем комбинацию из трех элементов
        if (combination.distinct().size == 3) { // Все элементы разные
            return 0f // Комбинация не удовлетворяет условиям
        }

        return 0f // Если ни одно из условий не выполнилось, возвращаем 0
    }

    private suspend fun rollSlot(liveData: MutableLiveData<List<Slot>>) {
        val currentList = liveData.value ?: return
        var currentPosition =
            currentList.reversed().indexOfFirst { it.relativePosition in 1 / 3f..2 / 3f }
        while (currentList.last().relativePosition > 5 / 6f) {
            delay(FREQUENCY)
            val positionChange = calculateSpeed(currentPosition) * FREQUENCY / 1000
            currentList.forEach { it.relativePosition = it.relativePosition - positionChange }
            liveData.value = currentList
            currentPosition = currentList.reversed().indexOfFirst { slot ->
                slot.relativePosition in 1 / 3f..2 / 3f
            }
        }
        val positionAdjust = currentList.last().relativePosition - 5 / 6f
        currentList.forEach { it.relativePosition = it.relativePosition - positionAdjust }
        liveData.value = currentList
    }

    private fun calculateSpeed(currentPosition: Int): Float {
        return currentPosition.toFloat().pow(1 / 2f)
    }

    private fun generateSlots() {
        val slotList1 = mutableListOf<Slot>().apply {
            addAll(
                _slot1LiveData.value?.filter { it.relativePosition in 0f..1f } ?: emptyList(),
            )
        }
        val slotList2 = mutableListOf<Slot>().apply {
            addAll(
                _slot2LiveData.value?.filter { it.relativePosition in 0f..1f } ?: emptyList(),
            )
        }
        val slotList3 = mutableListOf<Slot>().apply {
            addAll(
                _slot3LiveData.value?.filter { it.relativePosition in 0f..1f } ?: emptyList(),
            )
        }
        val combination = combinations.random()
        val constOffset = 1 / 6f
        for (i in slotList1.size..48) {
            slotList1.add(
                Slot(
                    i,
                    values.random(),
                    constOffset + i * constOffset * 2,
                ),
            )
            slotList2.add(
                Slot(
                    i,
                    values.random(),
                    constOffset + i * constOffset * 2,
                ),
            )
            slotList3.add(
                Slot(
                    i,
                    values.random(),
                    constOffset + i * constOffset * 2,
                ),
            )
        }
        for (i in 0..1) {
            slotList1.add(
                Slot(
                    slotList1.last().id + 1,
                    combination[0],
                    slotList1.last().relativePosition + 1 / 3f,
                ),
            )
            slotList2.add(
                Slot(
                    slotList2.last().id + 1,
                    combination[1],
                    slotList2.last().relativePosition + 1 / 3f,
                ),
            )
            slotList3.add(
                Slot(
                    slotList3.last().id + 1,
                    combination[2],
                    slotList3.last().relativePosition + 1 / 3f,
                ),
            )
        }
        _slot1LiveData.value = slotList1
        _slot2LiveData.value = slotList2
        _slot3LiveData.value = slotList3
    }

    internal enum class GameState {
        Idle, Rolling
    }
}
