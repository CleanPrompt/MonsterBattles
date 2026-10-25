package nl.monsterbattles.api.monsters

import nl.monsterbattles.api.features.monsters.MonsterService
import nl.monsterbattles.api.features.monsters.model.Element
import nl.monsterbattles.api.features.monsters.model.Stats
import nl.monsterbattles.api.features.monsters.repository.InMemoryMonsterRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MonsterServiceTest {
    @Test fun filteringSortingAndInvalidQueries() {
        val repo = InMemoryMonsterRepository()
        repo.create("Pyron", Element.FIRE, Stats(100, 30, 10, 110))
        repo.create("Aquara", Element.WATER, Stats(110, 25, 12, 95))
        repo.create("Ember", Element.FIRE, Stats(50, 20, 5, 120))
        val service = MonsterService(repo)
        assertEquals(listOf("Ember", "Pyron"), service.list("FIRE", "speed").map { it.name })
        assertEquals(listOf("Aquara", "Ember", "Pyron"), service.list(sort="name").map { it.name })
        assertEquals(listOf(1, 2, 3), service.list().map { it.id })
        assertFailsWith<IllegalArgumentException> { service.list(element="ICE") }
        assertFailsWith<IllegalArgumentException> { service.list(sort="unknown") }
    }
}
