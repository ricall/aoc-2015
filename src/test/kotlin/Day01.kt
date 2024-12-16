package org.ricall.day01

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day01 {
    private fun delta(char: String) = when(char) {
        "(" -> 1
        ")" -> -1
        else -> 0
    }

    private fun String.countFloors() = this.split("").map(::delta).sum()

    private fun String.indexOfFloor(targetFloor: Int): Int? {
        var floor = 0
        this.split("").forEachIndexed { index, ch ->
            floor += delta(ch)
            if (floor == targetFloor) {
                return index
            }
        }
        return null
    }

    @Test
    fun `part 1 test data`() {
        assertEquals(-3, ")())())".countFloors())
    }

    @Test
    fun `part 1`() {
        val result = File("./inputs/day01.txt").readText().countFloors()
        assertEquals(138, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(5, "()())".indexOfFloor(-1))
    }

    @Test
    fun `part 2`() {
        val result = File("./inputs/day01.txt").readText().indexOfFloor(-1)
        assertEquals(1771, result)
    }
}