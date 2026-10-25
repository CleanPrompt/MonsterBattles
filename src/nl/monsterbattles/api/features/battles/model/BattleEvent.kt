package nl.monsterbattles.api.features.battles.model

data class BattleEvent(val turn: Int, val attackerId: Int, val defenderId: Int, val damage: Int, val defenderRemainingHp: Int)
