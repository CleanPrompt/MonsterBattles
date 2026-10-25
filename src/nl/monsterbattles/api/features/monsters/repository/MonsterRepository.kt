package nl.monsterbattles.api.features.monsters.repository

import nl.monsterbattles.api.features.monsters.model.Element
import nl.monsterbattles.api.features.monsters.model.Monster
import nl.monsterbattles.api.features.monsters.model.Stats

interface MonsterRepository {
    fun findAll(): List<Monster>
    fun findById(id: Int): Monster?
    fun create(name: String, element: Element, stats: Stats): Monster
    fun update(id: Int, name: String, element: Element, stats: Stats): Monster?
    fun delete(id: Int): Boolean
}
