package org.ricall.day14

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private val PARSE_REGEX = """^(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""".toRegex()

private data class Reindeer(val name: String, val speed: Int, val fly: Int, val rest: Int) {
    fun distance(time: Int): Int {
        val duration = fly + rest
        val sprints = time / duration
        val remainingFlyTime = Math.min(time % duration, fly)

        return speed * (sprints * fly + remainingFlyTime)
    }
}

private fun parseInput(input: String): List<Reindeer> = input.lines().map { line ->
    val (name, speed, fly, rest) = PARSE_REGEX.find(line)?.destructured!!
    Reindeer(name, speed.toInt(), fly.toInt(), rest.toInt())
}

private fun fastestReindeer(input: String, time: Int) = parseInput(input).map { it.distance(time) }.max()

private fun maxPoints(input: String, time: Int): Int {
    val data = parseInput(input)
    val rankings = mutableMapOf<Reindeer, Int>()

    (1..time).forEach { currentTime ->
        val results = data.groupBy { it.distance(currentTime) }
        val max = results.keys.max()
        results[max]?.forEach { winner -> rankings.put(winner, rankings.getOrDefault(winner, 0) + 1) }
    }
    return rankings.maxOf { it.value }
}

class Day14 {
    private val TEST_DATA = """
        |Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
        |Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val distance = fastestReindeer(TEST_DATA, 1000)

        assertEquals(1120, distance)
    }

    @Test
    fun `part 1`() {
        val distance = fastestReindeer(File("./inputs/day14.txt").readText(), 2503)

        assertEquals(2640, distance)
    }

    @Test
    fun `part 2 test data`() {
        val points = maxPoints(TEST_DATA, 1000)

        assertEquals(689, points)
    }

    @Test
    fun `part 2`() {
        val points = maxPoints(File("./inputs/day14.txt").readText(), 2503)

        assertEquals(1102, points)
    }
}