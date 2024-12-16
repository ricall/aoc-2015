import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day03 {
    data class Point(val x: Int, val y: Int) {
        fun move(direction: Char) = when(direction) {
            '^' -> Point(x, y+1)
            '<' -> Point(x-1, y)
            '>' -> Point(x+1, y)
            'v' -> Point(x, y-1)
            else -> throw IllegalArgumentException("Unknown direction: '$direction'")
        }
    }

    private fun deliverPresents(directions: String): Int {
        var point = Point(0, 0)
        val visited = mutableSetOf<Point>(point)
        directions.toCharArray().forEach { direction ->
            point = point.move(direction)
            visited.add(point)
        }
        return visited.size
    }

    private fun roboDeliverPresents(directions: String): Int {
        var santa = Point(0, 0)
        var roboSanta = Point(0, 0)
        val visited = mutableSetOf<Point>(santa)
        for (index in directions.toCharArray().indices step 2) {
            santa = santa.move(directions[index])
            roboSanta = roboSanta.move(directions[index+1])

            visited.add(santa)
            visited.add(roboSanta)
        }
        return visited.size
    }

    @Test
    fun `part 1 test data`() {
        assertEquals(2, deliverPresents(">"))
        assertEquals(4, deliverPresents("^>v<"))
        assertEquals(2, deliverPresents("^v^v^v^v^v"))
    }

    @Test
    fun `part 1`() {
        val result = File("./inputs/day03.txt").readText().let(::deliverPresents)

        assertEquals(2565, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(11, roboDeliverPresents("^v^v^v^v^v"))
    }

    @Test
    fun `part 2`() {
        val result = File("./inputs/day03.txt").readText().let(::roboDeliverPresents)

        assertEquals(2639, result)
    }
}