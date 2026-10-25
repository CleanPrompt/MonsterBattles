package nl.monsterbattles.api.common

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(val code: String, val message: String)

class MonsterNotFoundException(val monsterId: Int) : RuntimeException("Monster $monsterId does not exist")
class InvalidQueryException(message: String) : IllegalArgumentException(message)
