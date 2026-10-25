package nl.monsterbattles.api.features.monsters.dto

import nl.monsterbattles.api.features.monsters.model.Monster
import kotlinx.serialization.Serializable

@Serializable
data class CreateMonsterRequest(val name: String, val element: String, val hp: Int, val attack: Int, val defense: Int, val speed: Int)

@Serializable
data class UpdateMonsterRequest(val name: String, val element: String, val hp: Int, val attack: Int, val defense: Int, val speed: Int)

@Serializable
data class MonsterResponse(val id: Int, val name: String, val element: String, val hp: Int, val attack: Int, val defense: Int, val speed: Int)

fun Monster.toResponse(): MonsterResponse = MonsterResponse(id, name, element.name, stats.hp, stats.attack, stats.defense, stats.speed)
