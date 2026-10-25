package nl.monsterbattles.api.features.monsters.repository

import nl.monsterbattles.api.features.monsters.model.Element
import nl.monsterbattles.api.features.monsters.model.Monster
import nl.monsterbattles.api.features.monsters.model.Stats
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ExposedMonsterRepository(private val db: Database) : MonsterRepository {
    private fun ResultRow.toMonster(): Monster = Monster(
        id = this[MonstersTable.id].value,
        name = this[MonstersTable.name],
        element = Element.valueOf(this[MonstersTable.element]),
        stats = Stats(this[MonstersTable.hp], this[MonstersTable.attack], this[MonstersTable.defense], this[MonstersTable.speed])
    )

    override fun findAll(): List<Monster> = transaction(db) {
        MonstersTable.selectAll().orderBy(MonstersTable.id).map { it.toMonster() }
    }

    override fun findById(id: Int): Monster? = transaction(db) {
        MonstersTable.selectAll().where { MonstersTable.id eq id }.singleOrNull()?.toMonster()
    }

    override fun create(name: String, element: Element, stats: Stats): Monster = transaction(db) {
        // Validate before writing.
        Monster(1, name, element, stats)
        val newId = MonstersTable.insertAndGetId {
            it[MonstersTable.name] = name
            it[MonstersTable.element] = element.name
            it[hp] = stats.hp
            it[attack] = stats.attack
            it[defense] = stats.defense
            it[speed] = stats.speed
        }.value
        Monster(newId, name, element, stats)
    }

    override fun update(id: Int, name: String, element: Element, stats: Stats): Monster? = transaction(db) {
        if (id <= 0) return@transaction null
        val updated = Monster(id, name, element, stats)
        val rows = MonstersTable.update({ MonstersTable.id eq id }) {
            it[MonstersTable.name] = name
            it[MonstersTable.element] = element.name
            it[hp] = stats.hp
            it[attack] = stats.attack
            it[defense] = stats.defense
            it[speed] = stats.speed
        }
        if (rows == 0) null else updated
    }

    override fun delete(id: Int): Boolean = transaction(db) {
        MonstersTable.deleteWhere { MonstersTable.id eq id } > 0
    }
}
