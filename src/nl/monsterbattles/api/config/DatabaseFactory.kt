package nl.monsterbattles.api.config

import nl.monsterbattles.api.features.monsters.repository.MonstersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.nio.file.Paths

object DatabaseFactory {
    fun connect(url: String = System.getenv("MONSTERBATTLES_DB_URL") ?: "jdbc:h2:file:./data/monsterbattles"): Database {
        if (url.startsWith("jdbc:h2:file:./data/")) Files.createDirectories(Paths.get("data"))
        val database = Database.connect(url, driver = "org.h2.Driver", user = "sa", password = "")
        transaction(database) { SchemaUtils.create(MonstersTable) }
        return database
    }
}
