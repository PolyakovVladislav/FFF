package com.fairyfo.frenzy.ui.models


data class Miner(
    val matrix: Array<Array<MinerItem>>,
    var gameState: GameState
) {

    enum class GameState {
        Finished, Playing
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Miner

        if (!matrix.contentDeepEquals(other.matrix)) return false

        return true
    }

    override fun hashCode(): Int {
        return matrix.contentDeepHashCode()
    }
}