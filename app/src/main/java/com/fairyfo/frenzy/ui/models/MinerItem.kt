package com.fairyfo.frenzy.ui.models


data class MinerItem(
    var discovered: Boolean,
    var animateDiscover: Boolean,
    var itemType: ItemType,
    val x: Int,
    val y: Int
) {

    enum class ItemType {
        Gold, Bomb
    }
}