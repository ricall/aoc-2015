package org.ricall.day22

import org.junit.jupiter.api.Test
import org.ricall.day22.Spell.*
import java.io.File
import java.util.*
import kotlin.test.assertEquals

private enum class Spell(
    val cost: Int,
    val action: ((GameState) -> Unit)? = null,
    val duration: Int = 0,
    val effect: ((GameState) -> Unit)? = null,
    val afterEffect: ((GameState) -> Unit)? = null,
) {
    MAGIC_MISSILE(cost = 53, action = { it.bossHitPoints -= 4 }),
    DRAIN(cost = 73, action = { it.bossHitPoints -= 2; it.hitPoints += 2 }),
    SHIELD(cost = 113, duration = 6, effect = { it.armor = 7 }, afterEffect = { it.armor = 0 }),
    POISON(cost = 173, duration = 6, effect = { it.bossHitPoints -= 3 }),
    RECHARGE(cost = 229, duration = 5, effect = { it.mana += 101 }),
}

private data class Effect(val spell: Spell, val remaining: Int)

private data class GameState(
    var bossHitPoints: Int,
    var bossDamage: Int,

    var hitPoints: Int,
    var mana: Int,
    var armor: Int = 0,
    var effects: List<Effect> = emptyList(),

    var manaSpent: Int = 0,
) {
    fun canCast(spell: Spell): Boolean = mana >= spell.cost && effects.none { it.spell == spell }

    fun castSpell(spell: Spell) {
        mana -= spell.cost
        manaSpent += spell.cost
        spell.action?.let { it(this) }
        if (spell.duration > 0) {
            effects += Effect(spell, spell.duration)
        }
    }

    fun applyEffects() {
        effects = effects.mapNotNull {
            it.spell.effect?.let { it(this) }
            when (it.remaining) {
                1 -> {
                    it.spell.afterEffect?.let { it(this) }
                    null
                }

                else -> it.copy(remaining = it.remaining - 1)
            }
        }
    }
}

private fun solve(gameState: GameState, hard: Boolean = false): Int {
    val gameStates = PriorityQueue<GameState>(compareBy { it.manaSpent })
    gameStates.add(gameState)
    var minimumMana = Int.MAX_VALUE

    while (gameStates.isNotEmpty()) {
        val candidate = gameStates.poll()
        if (hard) {
            if (candidate.hitPoints-- <= 0) {
                continue
            }
        }
        candidate.applyEffects()
        for (spell in entries) {
            if (!candidate.canCast(spell)) {
                continue
            }
            val current = candidate.copy()
            current.castSpell(spell)
            current.applyEffects()
            if (current.bossHitPoints <= 0) {
                minimumMana = Math.min(minimumMana, current.manaSpent)
                gameStates.removeIf { it.manaSpent < minimumMana }
            } else {
                current.hitPoints -= Math.max(1, current.bossDamage - current.armor)
                if (current.hitPoints > 0 && current.mana > 0 && current.manaSpent < minimumMana) {
                    gameStates.add(current)
                }
            }
        }
    }
    return minimumMana
}

private fun parseInput(input: String): GameState {
    val (bossHp, bossDamage) = input.lines().map { it.split(": ")[1].toInt() }
    return GameState(hitPoints = 50, mana = 500, bossHitPoints = bossHp, bossDamage = bossDamage)
}

class Day22 {
    private val initialState = parseInput(File("./inputs/day22.txt").readText())

    @Test
    fun `part 1`() {
        val result = solve(initialState)

        assertEquals(1824, result)
    }

    @Test
    fun `part 2`() {
        val result = solve(initialState, true)

        assertEquals(1937, result)
    }
}