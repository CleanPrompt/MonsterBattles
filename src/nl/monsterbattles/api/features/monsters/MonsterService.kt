package nl.monsterbattles.api.features.monsters

import nl.monsterbattles.api.common.InvalidQueryException
import nl.monsterbattles.api.features.monsters.model.Element
import nl.monsterbattles.api.features.monsters.model.Monster
import nl.monsterbattles.api.features.monsters.model.Stats
import nl.monsterbattles.api.features.monsters.dto.CreateMonsterRequest
import nl.monsterbattles.api.features.monsters.dto.UpdateMonsterRequest
import nl.monsterbattles.api.features.monsters.repository.MonsterRepository

class MonsterService(private val repository: MonsterRepository) {
    fun list(element: String? = null, sort: String? = null): List<Monster> {
        val filter = element?.let {
            try { Element.valueOf(it) } catch (_: IllegalArgumentException) {
                throw InvalidQueryException("Unknown element: $it")
            }
        }
        if (sort != null && sort !in setOf("name", "speed")) throw InvalidQueryException("Unknown sort: $sort")
        val entries = repository.findAll().filter { filter == null || it.element == filter }
        return when (sort) {
            "name" -> entries.sortedWith(compareBy<Monster> { it.name }.thenBy { it.id })
            "speed" -> entries.sortedWith(compareByDescending<Monster> { it.stats.speed }.thenBy { it.id })
            else -> entries.sortedBy { it.id }
        }
    }
    fun find(id: Int): Monster? = repository.findById(id)
    fun create(request: CreateMonsterRequest): Monster = repository.create(request.name, parseElement(request.element), request.stats())
    fun update(id: Int, request: UpdateMonsterRequest): Monster? = repository.update(id, request.name, parseElement(request.element), request.stats())
    fun delete(id: Int): Boolean = repository.delete(id)

    private fun parseElement(value: String): Element = try { Element.valueOf(value) }
    catch (_: IllegalArgumentException) { throw IllegalArgumentException("Unknown element: $value") }
    private fun CreateMonsterRequest.stats() = Stats(hp, attack, defense, speed)
    private fun UpdateMonsterRequest.stats() = Stats(hp, attack, defense, speed)
}
