import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day10 {
    private fun lookAndSay(input: Sequence<Char>) = sequence<String> {
        var count = 0
        var current: Char? = null
        input.forEach {
            when {
                current == null -> {
                    current = it
                    count = 1
                }
                current == it -> count++
                else -> {
                    yield("$count$current")
                    count = 1
                    current = it
                }
            }
        }
        if (current != null) {
            yield("$count$current")
        }
    }

    private fun nextSequence(input: String) = lookAndSay(input.asSequence()).joinToString("")

    @Test
    fun `part 1 test data`() {
        assertEquals("11", nextSequence("1"))
        assertEquals("21", nextSequence("11"))
        assertEquals("1211", nextSequence("21"))
        assertEquals("111221", nextSequence("1211"))
        assertEquals("312211", nextSequence("111221"))
    }

    @Test
    fun `part 1`() {
        var input = File("./inputs/day10.txt").readText()
        repeat(40) { input = nextSequence(input) }
        assertEquals(492982, input.length)
    }

    @Test
    fun `part 2`() {
        var input = File("./inputs/day10.txt").readText()
        repeat(50) { input = nextSequence(input) }
        assertEquals(6989950, input.length)
    }
}