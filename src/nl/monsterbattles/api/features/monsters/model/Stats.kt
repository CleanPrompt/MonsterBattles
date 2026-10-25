package nl.monsterbattles.api.features.monsters.model

data class Stats(val hp: Int, val attack: Int, val defense: Int, val speed: Int) {
    init {
        require(hp > 0) { "HP must be positive" }
        require(attack >= 0) { "Attack cannot be negative" }
        require(defense >= 0) { "Defense cannot be negative" }
        require(speed >= 0) { "Speed cannot be negative" }
    }
}
