package nl.monsterbattles.api.features.battles

class DamageCalculator {
    fun calculate(attack: Int, defense: Int): Int = maxOf(1, attack - defense)
}
