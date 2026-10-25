package nl.monsterbattles.api.config

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import nl.monsterbattles.api.common.ApiError
import nl.monsterbattles.api.common.InvalidQueryException
import nl.monsterbattles.api.common.MonsterNotFoundException

fun Application.configurePlugins() {
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = false; encodeDefaults = true }) }
    install(StatusPages) {
        exception<MonsterNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ApiError("MONSTER_NOT_FOUND", cause.message ?: "Monster does not exist"))
        }
        exception<InvalidQueryException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ApiError("INVALID_QUERY", cause.message ?: "Invalid query"))
        }
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ApiError("VALIDATION_ERROR", cause.message ?: "Invalid request"))
        }
        exception<BadRequestException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, ApiError("INVALID_JSON", "Malformed request"))
        }
        exception<ContentTransformationException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, ApiError("INVALID_JSON", "Malformed request"))
        }
        exception<SerializationException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, ApiError("INVALID_JSON", "Malformed request"))
        }
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled API error", cause)
            call.respond(HttpStatusCode.InternalServerError, ApiError("INTERNAL_ERROR", "Unexpected server error"))
        }
    }
}
