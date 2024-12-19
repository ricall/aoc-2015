import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private data class Point(val x: Int, val y: Int) {
    operator fun plus(direction: Direction) = Point(x + direction.delta.x, y + direction.delta.y)
}
private enum class Direction(val delta: Point) {
    NORTH(Point(0, -1)),
    NORTH_EAST(Point(1, -1)),
    EAST(Point(1, 0)),
    SOUTH_EAST(Point(1, 1)),
    SOUTH(Point(0, 1)),
    SOUTH_WEST(Point(-1, 1)),
    WEST(Point(-1, 0)),
    NORTH_WEST(Point(-1, -1)),
}

private class Grid(input: String) {
    private val height = input.lines().size
    private val width = input.lines().first().length
    private var lights = buildSet {
        input.lines().mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                if (c == '#') add(Point(x, y))
            }
        }
    }

    fun lightsOn() = lights.size

    fun advance(forceCornersOn: Boolean = false) {
        lights = buildSet {
            (0..<height).forEach { y ->
                (0..<width).forEach { x ->
                    val point = Point(x, y)
                    val lightsAround: Int = Direction.entries.map { if (lights.contains(point + it)) 1 else 0 }.sum()
                    when (lights.contains(point)) {
                        true -> if (lightsAround == 2 || lightsAround == 3) add(point)
                        else -> if (lightsAround == 3) add(point)
                    }
                }
            }
            if (forceCornersOn) {
                add(Point(0, 0))
                add(Point(0, height - 1))
                add(Point(width - 1, 0))
                add(Point(width - 1, height - 1))
            }
        }
    }

    override fun toString() = buildString {
        (0..<height).forEach { y ->
            (0..<width).forEach { x ->
                append(if (lights.contains(Point(x, y))) '#' else '.')
            }
            appendLine()
        }
    }
}

class Day18 {
    private val TEST_DATA = """
        |.#.#.#
        |...##.
        |#....#
        |..#...
        |#.#..#
        |####..""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val grid = Grid(TEST_DATA)
        repeat(4) { grid.advance() }
        println(grid)

        assertEquals(4, grid.lightsOn())
    }

    @Test
    fun `part 1`() {
        val grid = Grid(File("./inputs/day18.txt").readText())
        repeat(100) { grid.advance() }

        assertEquals(768, grid.lightsOn())
    }

    @Test
    fun `part 2`() {
        val grid = Grid(File("./inputs/day18.txt").readText())
        repeat(100) { grid.advance(true) }

        assertEquals(781, grid.lightsOn())
    }
}