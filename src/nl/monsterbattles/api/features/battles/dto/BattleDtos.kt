package nl.monsterbattles.api.features.battles.dto

import nl.monsterbattles.api.features.battles.model.BattleEvent
import nl.monsterbattles.api.features.battles.model.BattleResult
import kotlinx.serialization.Serializable

@Serializable
data class StartBattleRequest(val attackerId: Int, val defenderId: Int)

@Serializable
data class BattleEventResponse(val turn: Int, val attackerId: Int, val defenderId: Int, val damage: Int, val defenderRemainingHp: Int)

@Serializable
data class BattleResponse(
    val winnerId: Int, val rounds: Int, val turns: Int,
    val attackerRemainingHp: Int, val defenderRemainingHp: Int,
    val log: List<BattleEventResponse>
)

fun BattleEvent.toResponse() = BattleEventResponse(turn, attackerId, defenderId, damage, defenderRemainingHp)
fun BattleResult.toResponse() = BattleResponse(winnerId, rounds, turns, attackerRemainingHp, defenderRemainingHp, log.map { it.toResponse() })
