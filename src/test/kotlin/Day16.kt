package org.ricall.day16

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private val PARSE_REGEX = """^Sue (\d+): (.*)$""".toRegex()

private fun parseInput(input: String) = input.lines().map { line ->
    val (name, factText) = PARSE_REGEX.find(line)!!.destructured
    val facts = factText.split(", ").associate { fact ->
        val (key, value) = fact.split(": ")
        key to value.toInt()
    }
    name to facts
}

private fun matches(fact: Pair<String, Map<String, Int>>, detected: Map<String, Int>) =
    detected.all { (key, value) -> (fact.second[key] ?: value) == value }

private fun matchesPart2(fact: Pair<String, Map<String, Int>>, detected: Map<String, Int>) =
    detected.all { (key, value) ->
        when (key) {
            "cats", "trees" -> (fact.second[key] ?: (value + 1)) > value
            "pomeranians", "goldfish" -> (fact.second[key] ?: (value - 1)) < value
            else -> (fact.second[key] ?: value) == value
        }
    }

class Day16 {
    private val facts = parseInput(File("./inputs/day16.txt").readText())
    private val detected = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1,
    )

    @Test
    fun `part 1`() {
        val match = facts.filter { matches(it, detected) }
            .map { it.first.toInt() }

        assertEquals(listOf(373), match)
    }

    @Test
    fun `part 2`() {
        val match = facts.filter { matchesPart2(it, detected) }
            .map { it.first.toInt() }

        assertEquals(listOf(260), match)
    }
}