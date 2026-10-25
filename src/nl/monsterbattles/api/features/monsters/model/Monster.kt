package nl.monsterbattles.api.features.monsters.model

class Monster(val id: Int, val name: String, val element: Element, val stats: Stats) {
    init {
        require(id > 0) { "ID must be positive" }
        require(name.isNotBlank()) { "Name is required" }
    }
}
