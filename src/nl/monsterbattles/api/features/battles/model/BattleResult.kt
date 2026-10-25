package nl.monsterbattles.api.features.battles.model

data class BattleResult(
    val winnerId: Int,
    val rounds: Int,
    val turns: Int,
    val attackerRemainingHp: Int,
    val defenderRemainingHp: Int,
    val log: List<BattleEvent>
)
