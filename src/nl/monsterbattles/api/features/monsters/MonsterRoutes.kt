package nl.monsterbattles.api.features.monsters

import nl.monsterbattles.api.common.MonsterNotFoundException
import nl.monsterbattles.api.features.monsters.dto.CreateMonsterRequest
import nl.monsterbattles.api.features.monsters.dto.UpdateMonsterRequest
import nl.monsterbattles.api.features.monsters.dto.toResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.monsterRoutes(service: MonsterService) {
    route("/monsters") {
        get {
            val result = service.list(call.request.queryParameters["element"], call.request.queryParameters["sort"])
            call.respond(result.map { it.toResponse() })
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid monster ID")
            require(id > 0) { "Invalid monster ID" }
            call.respond((service.find(id) ?: throw MonsterNotFoundException(id)).toResponse())
        }
        post {
            val monster = service.create(call.receive<CreateMonsterRequest>())
            call.response.header(HttpHeaders.Location, "/monsters/${monster.id}")
            call.respond(HttpStatusCode.Created, monster.toResponse())
        }
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid monster ID")
            require(id > 0) { "Invalid monster ID" }
            val monster = service.update(id, call.receive<UpdateMonsterRequest>()) ?: throw MonsterNotFoundException(id)
            call.respond(monster.toResponse())
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid monster ID")
            require(id > 0) { "Invalid monster ID" }
            if (!service.delete(id)) throw MonsterNotFoundException(id)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
