package org.ricall.day17

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun containerPermutations(store: Int, containers: List<Int>) = buildList {
    (0..Math.pow(2.0, containers.size.toDouble()).toInt()).forEach {
        val mask = it
        val list = containers.mapIndexedNotNull { index, container ->
            if (mask and (1 shl index) != 0) container else null
        }
        if (list.sum() == store) {
            add(list)
        }
    }
}

private fun minimumNumberOfContainers(store: Int, containers: List<Int>): Int {
    val permutations = containerPermutations(store, containers)
    val min = permutations.minOf { it.size }
    return permutations.count { it.size == min }
}

class Day17 {
    @Test
    fun `part 1 test data`() {
        val result = containerPermutations(25, listOf(20, 15, 10, 5, 5))

        assertEquals(listOf(
            listOf(15, 10),
            listOf(20, 5),
            listOf(20, 5),
            listOf(15, 5, 5),
        ), result)
    }

    @Test
    fun `part 1`() {
        val containers = File("./inputs/day17.txt").readLines().map { it.toInt() }
        val result = containerPermutations(150, containers)

        assertEquals(654, result.size)
    }

    @Test
    fun `part 2 test data`() {
        val result = minimumNumberOfContainers(25, listOf(20, 15, 10, 5, 5))

        assertEquals(3, result)
    }

    @Test
    fun `part 2`() {
        val containers = File("./inputs/day17.txt").readLines().map { it.toInt() }
        val result = minimumNumberOfContainers(150, containers)

        assertEquals(57, result)
    }
}