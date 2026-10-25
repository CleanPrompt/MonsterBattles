package nl.monsterbattles.api.battles

import nl.monsterbattles.api.features.battles.BattleService
import nl.monsterbattles.api.features.monsters.model.Element
import nl.monsterbattles.api.features.monsters.model.Monster
import nl.monsterbattles.api.features.monsters.model.Stats
import nl.monsterbattles.api.features.monsters.repository.InMemoryMonsterRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BattleServiceTest {
    @Test fun pyronBeatsAquaraWithoutChangingStoredStats() {
        val repo = InMemoryMonsterRepository()
        val pyron = repo.create("Pyron", Element.FIRE, Stats(100, 30, 10, 110))
        val aquara = repo.create("Aquara", Element.WATER, Stats(110, 25, 12, 95))
        val result = BattleService(repo).start(pyron.id, aquara.id)
        assertEquals(pyron.id, result.winnerId)
        assertEquals(7, result.rounds)
        assertEquals(13, result.turns)
        assertEquals(13, result.log.size)
        assertEquals(10, result.attackerRemainingHp)
        assertEquals(0, result.defenderRemainingHp)
        assertEquals(100, repo.findById(pyron.id)!!.stats.hp)
        assertEquals(110, repo.findById(aquara.id)!!.stats.hp)
    }
    @Test fun equalSpeedStartsWithFirstSelectedMonster() {
        val first = Monster(1, "First", Element.FIRE, Stats(5, 99, 0, 100))
        val second = Monster(2, "Second", Element.WATER, Stats(5, 99, 0, 100))
        val result = BattleService(InMemoryMonsterRepository()).fight(first, second)
        assertEquals(1, result.log.first().attackerId)
        assertEquals(1, result.turns)
        assertEquals(1, result.rounds)
    }
    @Test fun sameMonsterRejected() {
        val repo = InMemoryMonsterRepository()
        assertFailsWith<IllegalArgumentException> { BattleService(repo).start(1, 1) }
    }
}
