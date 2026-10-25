package nl.monsterbattles.api.features.battles

import nl.monsterbattles.api.features.battles.dto.StartBattleRequest
import nl.monsterbattles.api.features.battles.dto.toResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.battleRoutes(service: BattleService) {
    post("/battles") {
        val request = call.receive<StartBattleRequest>()
        call.respond(HttpStatusCode.OK, service.start(request.attackerId, request.defenderId).toResponse())
    }
}
