package org.ricall.day20

import org.junit.jupiter.api.Test
import org.ricall.utils.permutations
import java.io.File
import kotlin.Int.Companion.MAX_VALUE
import kotlin.Int.Companion.MIN_VALUE
import kotlin.test.assertEquals

private data class Item(
    val group: String,
    val name: String,
    val cost: Int,
    val damage: Int,
    val armor: Int
)

private val items = listOf(
    Item("weapons", "Dagger", 8, 4, 0),
    Item("weapons", "Shortsword", 10, 5, 0),
    Item("weapons", "Warhammer", 25, 6, 0),
    Item("weapons", "Longsword", 40, 7, 0),
    Item("weapons", "Greataxe", 74, 8, 0),

    Item("armour", "None", 0, 0, 0),
    Item("armour", "Leather", 13, 0, 1),
    Item("armour", "Chainmail", 31, 0, 2),
    Item("armour", "Splintmail", 53, 0, 3),
    Item("armour", "Bandedmail", 75, 0, 4),
    Item("armour", "Platemail", 102, 0, 5),

    Item("rings", "None 1", 0, 0, 0),
    Item("rings", "None 2", 0, 0, 0),
    Item("rings", "Damage +1", 25, 1, 0),
    Item("rings", "Damage +2", 50, 2, 0),
    Item("rings", "Damage +3", 100, 3, 0),
    Item("rings", "Defense +1", 20, 0, 1),
    Item("rings", "Defense +2", 40, 0, 2),
    Item("rings", "Defense +3", 80, 0, 3),
)

private data class Player(val name: String, var hitPoints: Int, val damage: Int, val armor: Int) {
    fun spar(other: Player): Boolean {
        hitPoints -= Math.max(other.damage - armor, 1)
        return hitPoints > 0
    }
}

private fun parseInput(name: String, input: String): Player {
    val props = input.lines().map {
        val (field, value) = it.split(": ")
        field to value.toInt()
    }.toMap()

    return Player(name, props["Hit Points"]!!, props["Damage"]!!, props["Armor"]!!)
}

private fun fight(players: List<Player>): String {
    var round = 1
    while (true) {
        val (p1, p2) = players[round % 2] to players[(round + 1) % 2]
        if (!p1.spar(p2)) {
           return p2.name
        }
        round++
    }
}

private fun itemPermutations() = sequence {
    val weapons = items.filter { it.group == "weapons" }
    val armourList = items.filter { it.group == "armour" }
    val ringsList = items.filter { it.group == "rings" }

    for (weapon in weapons) {
        for (armour in armourList) {
            for (rings in ringsList.permutations(2)) {
                val items = listOf(weapon, armour) + rings
                val cost = items.sumOf { it.cost }
                val player = Player("player", 100, items.sumOf { it.damage }, items.sumOf { it.armor })
                yield(player to cost)
            }
        }
    }
}

private fun leastAmountOfGoldToWin(boss: Player) = itemPermutations().minOf { (player, cost) ->
    if (fight(listOf(player, boss.copy())) == "player") cost else MAX_VALUE
}

private fun mostAmountOfGoldToLose(boss: Player) = itemPermutations().maxOf { (player, cost) ->
    if (fight(listOf(player, boss.copy())) == "boss") cost else MIN_VALUE
}

class Day21 {
    private val BOSS = """
        |Hit Points: 12
        |Damage: 7
        |Armor: 2""".trimMargin()

    private val PLAYER = """
        |Hit Points: 8
        |Damage: 5
        |Armor: 5""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val boss = parseInput("boss", BOSS)
        val player = parseInput("player", PLAYER)

        assertEquals("player", fight(listOf(player, boss)))
    }

    @Test
    fun `part 1`() {
        val boss = parseInput("boss", File("./inputs/day21.txt").readText())
        val result = leastAmountOfGoldToWin(boss)

        assertEquals(111, result)
    }

    @Test
    fun `part 2`() {
        val boss = parseInput("boss", File("./inputs/day21.txt").readText())
        val result = mostAmountOfGoldToLose(boss)

        assertEquals(188, result)
    }
}