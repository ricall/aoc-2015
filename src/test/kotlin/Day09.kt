package org.ricall.day09

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun <T> Iterable<T>.combinations(length: Int): Sequence<List<T>> = sequence {
    val pool = this@combinations as? List<T> ?: toList()
    val n = pool.size
    if (length > n) return@sequence
    val indices = IntArray(length) { it }
    while (true) {
        yield(indices.map { pool[it] })
        var i = length
        do {
            i--
            if (i == -1) return@sequence
        } while (indices[i] == i + n - length)
        indices[i]++
        for (j in i + 1 until length) indices[j] = indices[j - 1] + 1
    }
}

private fun <T> Iterable<T>.permutations(length: Int? = null): Sequence<List<T>> =
    sequence {
        val pool = this@permutations as? List<T> ?: toList()
        val n = pool.size
        val r = length ?: n
        if (r > n) return@sequence
        val indices = IntArray(n) { it }
        val cycles = IntArray(r) { n - it }
        yield(List(r) { pool[indices[it]] })
        if (n == 0) return@sequence
        cyc@ while (true) {
            for (i in r - 1 downTo 0) {
                cycles[i]--
                if (cycles[i] == 0) {
                    val temp = indices[i]
                    for (j in i until n - 1) indices[j] = indices[j + 1]
                    indices[n - 1] = temp
                    cycles[i] = n - i
                } else {
                    val j = n - cycles[i]
                    indices[i] = indices[j].also { indices[j] = indices[i] }
                    yield(List(r) { pool[indices[it]] })
                    continue@cyc
                }
            }
            return@sequence
        }
    }

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