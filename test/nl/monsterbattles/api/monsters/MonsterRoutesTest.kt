package nl.monsterbattles.api.monsters

import nl.monsterbattles.api.module
import nl.monsterbattles.api.features.monsters.repository.InMemoryMonsterRepository
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.ktor.client.statement.bodyAsText

class MonsterRoutesTest {
    @Test fun createReadUpdateDeleteAndNotFound() = testApplication {
        application { module(InMemoryMonsterRepository()) }
        val create = client.post("/monsters") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Zephyra","element":"WIND","hp":90,"attack":20,"defense":8,"speed":120}""")
        }
        assertEquals(HttpStatusCode.Created, create.status)
        val id = Json.parseToJsonElement(create.bodyAsText()).jsonObject.getValue("id").jsonPrimitive.content.toInt()
        assertEquals("/monsters/$id", create.headers[HttpHeaders.Location])
        assertEquals(HttpStatusCode.OK, client.get("/monsters/$id").status)
        val update = client.put("/monsters/$id") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Zephyra","element":"WIND","hp":90,"attack":22,"defense":8,"speed":120}""")
        }
        assertEquals(HttpStatusCode.OK, update.status)
        assertTrue(update.bodyAsText().contains("\"attack\":22"))
        assertEquals(HttpStatusCode.NoContent, client.delete("/monsters/$id").status)
        assertEquals(HttpStatusCode.NotFound, client.get("/monsters/$id").status)
    }
    @Test fun invalidQueriesReturnUniformErrors() = testApplication {
        application { module(InMemoryMonsterRepository()) }
        val bad = client.get("/monsters?element=ICE")
        assertEquals(HttpStatusCode.BadRequest, bad.status)
        assertTrue(bad.bodyAsText().contains("INVALID_QUERY"))
        val badId = client.get("/monsters/nope")
        assertEquals(HttpStatusCode.BadRequest, badId.status)
        assertTrue(badId.bodyAsText().contains("VALIDATION_ERROR"))
    }
}
