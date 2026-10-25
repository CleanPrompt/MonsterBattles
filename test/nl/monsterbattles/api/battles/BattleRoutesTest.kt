package nl.monsterbattles.api.battles

import nl.monsterbattles.api.module
import nl.monsterbattles.api.features.monsters.model.Element
import nl.monsterbattles.api.features.monsters.model.Stats
import nl.monsterbattles.api.features.monsters.repository.InMemoryMonsterRepository
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.ktor.client.statement.bodyAsText

class BattleRoutesTest {
    private fun seed(): InMemoryMonsterRepository = InMemoryMonsterRepository().apply {
        create("Pyron", Element.FIRE, Stats(100, 30, 10, 110))
        create("Aquara", Element.WATER, Stats(110, 25, 12, 95))
    }
    @Test fun completeReferenceBattle() = testApplication {
        application { module(seed()) }
        val response = client.post("/battles") {
            contentType(ContentType.Application.Json)
            setBody("""{"attackerId":1,"defenderId":2}""")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val result = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals("1", result.getValue("winnerId").jsonPrimitive.content)
        assertEquals("7", result.getValue("rounds").jsonPrimitive.content)
        assertEquals("13", result.getValue("turns").jsonPrimitive.content)
        assertEquals("10", result.getValue("attackerRemainingHp").jsonPrimitive.content)
        assertEquals("0", result.getValue("defenderRemainingHp").jsonPrimitive.content)
        assertEquals(13, result.getValue("log").jsonArray.size)
    }
    @Test fun invalidAndMissingMonsterReturn400And404() = testApplication {
        application { module(seed()) }
        suspend fun postBattle(json: String) = client.post("/battles") {
            contentType(ContentType.Application.Json); setBody(json)
        }
        val invalid = postBattle("""{"attackerId":1,"defenderId":1}""")
        assertEquals(HttpStatusCode.BadRequest, invalid.status)
        assertTrue(invalid.bodyAsText().contains("VALIDATION_ERROR"))
        val missing = postBattle("""{"attackerId":1,"defenderId":999}""")
        assertEquals(HttpStatusCode.NotFound, missing.status)
        assertTrue(missing.bodyAsText().contains("MONSTER_NOT_FOUND"))
        val malformed = postBattle("{broken")
        assertEquals(HttpStatusCode.BadRequest, malformed.status)
    }
}
