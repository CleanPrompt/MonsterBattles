package nl.monsterbattles.api.battles

import nl.monsterbattles.api.features.battles.DamageCalculator
import kotlin.test.Test
import kotlin.test.assertEquals

class DamageCalculatorTest {
    private val damage = DamageCalculator()
    @Test fun normalDamage() { assertEquals(18, damage.calculate(30, 12)) }
    @Test fun minimumDamage() { assertEquals(1, damage.calculate(0, 100)) }
    @Test fun equalAttackAndDefense() { assertEquals(1, damage.calculate(20, 20)) }
}
