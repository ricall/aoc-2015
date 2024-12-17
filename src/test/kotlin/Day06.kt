import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private val SWITCH_REGEX = Regex("""turn (on|off) (\d+),(\d+) through (\d+),(\d+)""")
private val TOGGLE_REGEX = Regex("""toggle (\d+),(\d+) through (\d+),(\d+)""")

class Day06 {
    class Lights(width: Int, height: Int) {
        private val lights = Array(height) { BooleanArray(width) }
        private fun switch(on: Boolean, x1: Int, y1: Int, x2: Int, y2: Int) {
            (y1..y2).forEach { y ->
                (x1..x2).forEach { x ->
                    lights[y][x] = on
                }
            }
        }
        private fun toggle(x1: Int, y1: Int, x2: Int, y2: Int) {
            (y1..y2).forEach { y ->
                (x1..x2).forEach { x ->
                    lights[y][x] = !lights[y][x]
                }
            }
        }

        fun execute(text: String): Int {
            text.lines().forEach { line ->
                SWITCH_REGEX.matchEntire(line)?.destructured?.let { (action, x1, y1, x2, y2) ->
                    switch(action == "on", x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
                }
                TOGGLE_REGEX.matchEntire(line)?.destructured?.let { (x1, y1, x2, y2) ->
                    toggle(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
                }
            }
            return lights.sumOf { it.count { it } }
        }
    }

    class Lights2(width: Int, height: Int) {
        private val lights = Array(height) { IntArray(width) }
        private fun switch(value: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
            (y1..y2).forEach { y ->
                (x1..x2).forEach { x ->
                    val newLight = lights[y][x] + value
                    if (newLight < 0) {
                        lights[y][x] = 0
                    } else {
                        lights[y][x] = newLight
                    }
                }
            }
        }

        fun execute(text: String): Int {
            text.lines().forEach { line ->
                SWITCH_REGEX.matchEntire(line)?.destructured?.let { (action, x1, y1, x2, y2) ->
                    switch(if (action == "on") 1 else -1, x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
                }
                TOGGLE_REGEX.matchEntire(line)?.destructured?.let { (x1, y1, x2, y2) ->
                    switch(2, x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
                }
            }
            return lights.sumOf { it.sum() }
        }
    }

    private val TEST_DATA = """
        |turn on 0,0 through 999,999
        |toggle 0,0 through 999,0
        |turn off 499,499 through 500,500""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = Lights(1000, 1000).execute(TEST_DATA)
        assertEquals(1_000_000 - 1000 - 4, result)
    }

    @Test
    fun `part 1`() {
        val result = Lights(1000, 1000).execute(File("./inputs/day06.txt").readText())
        assertEquals(400410, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = Lights2(1000, 1000).execute(TEST_DATA)
        assertEquals(1_000_000 + 2 * 1000 - 4, result)
    }

    @Test
    fun `part 2`() {
        val result = Lights2(1000, 1000).execute(File("./inputs/day06.txt").readText())
        assertEquals(15343601, result)
    }
}