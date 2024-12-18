package org.ricall.day09

import org.junit.jupiter.api.Test
import org.ricall.utils.permutations
import java.io.File
import kotlin.test.assertEquals


private fun minMaxRoute(input: String): Pair<Int, Int> {
    val edges = input.lines().map { it.split(" = ") }.flatMap { (from, distance) ->
        val (fromCity, toCity) = from.split(" to ")
        val totalDistance = distance.toInt()
        listOf(fromCity to (toCity to totalDistance), toCity to (fromCity to totalDistance))
    }.groupBy { it.first }.mapValues { (_, values) -> values.map { it.second }.toMap() }

    val locations = edges.keys.toList()
    val permutations = locations.permutations(locations.size)

    val lengths = permutations.map { permutation ->
        permutation.windowed(2).sumOf { (from, to) -> edges[from]?.get(to) ?: 0 }
    }
    return lengths.min() to lengths.max()
}

class Day09 {
    private val TEST_DATA = """
        |London to Dublin = 464
        |London to Belfast = 518
        |Dublin to Belfast = 141""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val (min, _) = minMaxRoute(TEST_DATA)

        assertEquals(605, min)
    }

    @Test
    fun `part 1`() {
        val (min, _) = minMaxRoute(File("./inputs/day09.txt").readText())

        assertEquals(207, min)
    }

    @Test
    fun `part 2 test data`() {
        val (_, max) = minMaxRoute(TEST_DATA)

        assertEquals(982, max)
    }

    @Test
    fun `part 2`() {
        val (_, max) = minMaxRoute(File("./inputs/day09.txt").readText())

        assertEquals(804, max)
    }
}