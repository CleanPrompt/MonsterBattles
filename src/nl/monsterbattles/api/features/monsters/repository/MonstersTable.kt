package nl.monsterbattles.api.features.monsters.repository

import org.jetbrains.exposed.dao.id.IntIdTable

object MonstersTable : IntIdTable("monsters") {
    val name = varchar("name", 120)
    val element = varchar("element", 12)
    val hp = integer("hp")
    val attack = integer("attack")
    val defense = integer("defense")
    val speed = integer("speed")
}
