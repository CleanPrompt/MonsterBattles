package nl.monsterbattles.api.monsters

import nl.monsterbattles.api.config.DatabaseFactory
import nl.monsterbattles.api.features.monsters.model.Element
import nl.monsterbattles.api.features.monsters.model.Stats
import nl.monsterbattles.api.features.monsters.repository.ExposedMonsterRepository
import nl.monsterbattles.api.features.monsters.repository.InMemoryMonsterRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import java.nio.file.Files

class MonsterRepositoryTest {
    @Test fun inMemoryCrud() {
        val repo = InMemoryMonsterRepository()
        val created = repo.create("Pyron", Element.FIRE, Stats(100, 30, 10, 110))
        assertEquals(1, created.id)
        assertNotNull(repo.findById(1))
        assertEquals(31, repo.update(1, "Pyron", Element.FIRE, Stats(100, 31, 10, 110))!!.stats.attack)
        assertTrue(repo.delete(1))
        assertFalse(repo.delete(1))
        assertNull(repo.findById(1))
    }
    @Test fun h2FilePersistsAcrossNewConnections() {
        val folder = Files.createTempDirectory("monsterbattles-db-")
        val url = "jdbc:h2:file:${folder.resolve("monsters").toAbsolutePath()}"
        val repo1 = ExposedMonsterRepository(DatabaseFactory.connect(url))
        val saved = repo1.create("Zephyra", Element.WIND, Stats(90, 20, 8, 120))
        val repo2 = ExposedMonsterRepository(DatabaseFactory.connect(url))
        assertEquals("Zephyra", repo2.findById(saved.id)?.name)
    }
}
