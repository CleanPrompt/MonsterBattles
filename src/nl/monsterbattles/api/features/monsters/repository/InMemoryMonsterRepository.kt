package nl.monsterbattles.api.features.monsters.repository

import nl.monsterbattles.api.features.monsters.model.Element
import nl.monsterbattles.api.features.monsters.model.Monster
import nl.monsterbattles.api.features.monsters.model.Stats

class InMemoryMonsterRepository : MonsterRepository {
    private val data = linkedMapOf<Int, Monster>()
    private var nextId = 1

    @Synchronized override fun findAll(): List<Monster> = data.values.toList()
    @Synchronized override fun findById(id: Int): Monster? = data[id]
    @Synchronized override fun create(name: String, element: Element, stats: Stats): Monster {
        val monster = Monster(nextId++, name, element, stats)
        data[monster.id] = monster
        return monster
    }
    @Synchronized override fun update(id: Int, name: String, element: Element, stats: Stats): Monster? {
        if (!data.containsKey(id)) return null
        return Monster(id, name, element, stats).also { data[id] = it }
    }
    @Synchronized override fun delete(id: Int): Boolean = data.remove(id) != null
}
