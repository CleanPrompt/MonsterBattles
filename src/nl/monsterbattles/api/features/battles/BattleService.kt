package nl.monsterbattles.api.features.battles

import nl.monsterbattles.api.common.MonsterNotFoundException
import nl.monsterbattles.api.features.monsters.model.Monster
import nl.monsterbattles.api.features.monsters.repository.MonsterRepository
import nl.monsterbattles.api.features.battles.model.BattleEvent
import nl.monsterbattles.api.features.battles.model.BattleResult

class BattleService(private val repository: MonsterRepository, private val calculator: DamageCalculator = DamageCalculator()) {
    fun start(attackerId: Int, defenderId: Int): BattleResult {
        require(attackerId > 0 && defenderId > 0) { "IDs must be positive" }
        require(attackerId != defenderId) { "Choose two different monsters" }
        val first = repository.findById(attackerId) ?: throw MonsterNotFoundException(attackerId)
        val second = repository.findById(defenderId) ?: throw MonsterNotFoundException(defenderId)
        return fight(first, second)
    }

    // Framework-independent engine; start() adds repository lookup for the HTTP use case.
    fun fight(attacker: Monster, defender: Monster): BattleResult {
        require(attacker.id != defender.id) { "Choose two different monsters" }
        var attackerHp = attacker.stats.hp
        var defenderHp = defender.stats.hp
        val firstIsAttacker = attacker.stats.speed >= defender.stats.speed
        var attackerTurn = firstIsAttacker
        val events = mutableListOf<BattleEvent>()
        var rounds = 0
        while (attackerHp > 0 && defenderHp > 0) {
            if (events.size % 2 == 0) rounds++
            val acting = if (attackerTurn) attacker else defender
            val target = if (attackerTurn) defender else attacker
            val damage = calculator.calculate(acting.stats.attack, target.stats.defense)
            if (attackerTurn) defenderHp = maxOf(0, defenderHp - damage)
            else attackerHp = maxOf(0, attackerHp - damage)
            events += BattleEvent(events.size + 1, acting.id, target.id, damage, if (attackerTurn) defenderHp else attackerHp)
            attackerTurn = !attackerTurn
        }
        return BattleResult(
            winnerId = if (attackerHp > 0) attacker.id else defender.id,
            rounds = rounds,
            turns = events.size,
            attackerRemainingHp = attackerHp,
            defenderRemainingHp = defenderHp,
            log = events.toList()
        )
    }
}
