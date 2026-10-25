package nl.monsterbattles.api

import nl.monsterbattles.api.features.battles.BattleService
import nl.monsterbattles.api.features.battles.battleRoutes
import nl.monsterbattles.api.features.monsters.MonsterService
import nl.monsterbattles.api.features.monsters.monsterRoutes
import nl.monsterbattles.api.features.monsters.model.Element
import nl.monsterbattles.api.features.monsters.model.Stats
import nl.monsterbattles.api.features.monsters.repository.ExposedMonsterRepository
import nl.monsterbattles.api.features.monsters.repository.InMemoryMonsterRepository
import nl.monsterbattles.api.features.monsters.repository.MonsterRepository
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import nl.monsterbattles.api.config.DatabaseFactory
import nl.monsterbattles.api.config.configurePlugins

fun main() {
    embeddedServer(Netty, host = "0.0.0.0", port = System.getenv("PORT")?.toIntOrNull() ?: 8080) { module() }.start(wait = true)
}

fun Application.module(repository: MonsterRepository = defaultRepository()) {
    configurePlugins()
    val monsterService = MonsterService(repository)
    val battleService = BattleService(repository)
    routing {
        monsterRoutes(monsterService)
        battleRoutes(battleService)
    }
}

fun defaultRepository(): MonsterRepository {
    val repository: MonsterRepository = if (System.getenv("MONSTERBATTLES_STORAGE") == "memory") {
        InMemoryMonsterRepository()
    } else {
        ExposedMonsterRepository(DatabaseFactory.connect())
    }
    // Idempotent only for an empty repository: never duplicate or reset user data.
    if (repository.findAll().isEmpty()) {
        repository.create("Pyron", Element.FIRE, Stats(100, 30, 10, 110))
        repository.create("Aquara", Element.WATER, Stats(110, 25, 12, 95))
    }
    return repository
}
