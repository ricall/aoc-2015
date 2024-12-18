package org.ricall.day15

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private val PARSE_REGEX =
    """(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)""".toRegex()

class Day15 {
    data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavour: Int,
        val texture: Int,
        val calories: Int
    )

    private fun parseIngredient(line: String): Ingredient {
        val (name, capacity, durability, flavour, texture, calories) = PARSE_REGEX.find(line)?.destructured!!
        return Ingredient(
            name,
            capacity.toInt(),
            durability.toInt(),
            flavour.toInt(),
            texture.toInt(),
            calories.toInt()
        )
    }

    private fun parseInput(input: String): Map<String, Ingredient> =
        input.lines().map(::parseIngredient).associateBy { it.name }

    private val TEST_DATA = """
        |Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
        |Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3""".trimMargin()

    private fun getCalories(ingredients: Map<String, Ingredient>, counts: Map<String, Int>) =
        counts.entries.sumOf { (name, count) -> (ingredients[name]?.calories ?: 0) * count }

    private fun scoreCookie(ingredients: Map<String, Ingredient>, counts: Map<String, Int>): Int {
        val results = listOf(
            Ingredient::capacity,
            Ingredient::durability,
            Ingredient::flavour,
            Ingredient::texture
        ).map { field ->
            val value = counts.entries.sumOf { (name, count) -> (ingredients[name]?.let { field(it) } ?: 0) * count }
            if (value < 0) 0 else value
        }
        return results.fold(1) { score, value -> score * value }
    }

    private fun cookiePermutations(ingredients: Map<String, Ingredient>) = sequence<Map<String, Int>> {
        val keys = ingredients.keys.toMutableList()
        val remainder = keys.removeLast()   // last item in the ingredient list always sums upto 100

        val map: MutableMap<String, Int> = keys.map { it to 1 }.toMap().toMutableMap()
        var index = keys.size - 1
        while (index >= 0) {
            map[remainder] = 100 - keys.sumOf { map[it] ?: 0 } // Ensure all ingredients add to 100 teaspoons
            yield(map)

            val max = 100 - keys.subList(0, index).sumOf { map[it] ?: 0 } - keys.size
            val value = (map[keys[index]] ?: 0) + 1
            if (value > max) {
                map[keys[index]] = 1        // rollover
                index--                     // we are now incrementing the previous column
            } else {
                map[keys[index]] = value
                index = keys.size - 1       // start incrementing from the right
            }
        }
    }

    private fun bestCookieScore(ingredients: Map<String, Ingredient>) = cookiePermutations(ingredients)
        .maxOf { counts -> scoreCookie(ingredients, counts) }

    private fun bestCookieScoreWith500Calories(ingredients: Map<String, Ingredient>) = cookiePermutations(ingredients)
        .maxOf { counts ->
            when (getCalories(ingredients, counts)) {
                500 -> scoreCookie(ingredients, counts)
                else -> 0
            }
        }

    @Test
    fun `part 1 test cookie scoring`() {
        val ingredients = parseInput(TEST_DATA)
        val score = scoreCookie(ingredients, mapOf("Butterscotch" to 44, "Cinnamon" to 56))

        assertEquals(62842880, score)
    }

    @Test
    fun `part 1 test data`() {
        val ingredients = parseInput(TEST_DATA)

        assertEquals(62842880, bestCookieScore(ingredients))
    }

    @Test
    fun `part 1`() {
        val ingredients = parseInput(File("./inputs/day15.txt").readText())

        assertEquals(222870, bestCookieScore(ingredients))
    }

    @Test
    fun `part 2 test data`() {
        val ingredients = parseInput(TEST_DATA)

        assertEquals(57600000, bestCookieScoreWith500Calories(ingredients))
    }

    @Test
    fun `part 2`() {
        val ingredients = parseInput(File("./inputs/day15.txt").readText())

        assertEquals(117936, bestCookieScoreWith500Calories(ingredients))
    }
}