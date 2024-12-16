import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day05 {
    private val VOWELS = listOf("a", "e", "i", "o", "u")
    private fun String.isNice(): Boolean {
        if (this.windowed(1).map { if (VOWELS.contains(it)) 1 else 0 }.sum() < 3) {
            return false
        }
        var doubles = false
        this.windowed(2).forEach {
            if (it[0] == it[1]) {
                doubles = true
            }
            if (listOf("ab", "cd", "pq", "xy").contains(it)) {
                return false
            }
        }
        return doubles
    }

    private fun String.isNice2(): Boolean {
        val text = this
        var lastDouble = ""
        val map = buildMap<String, Int> {
            text.windowed(2).forEach {
                if (it != lastDouble) {
                    put(it, getOrDefault(it, 0) + 1)
                    lastDouble = it
                } else {
                    lastDouble = ""
                }
            }
        }
        val pair = map.values.any { it > 1 }
        val repeats = text.windowed(3).any { it[0] == it[2] }

        return pair && repeats
    }

    @Test
    fun `part 1 test data`() {
        assertEquals(true, "ugknbfddgicrmopn".isNice())
        assertEquals(true, "aaa".isNice())
        assertEquals(false, "jchzalrnumimnmhp".isNice())
        assertEquals(false, "haegwjzuvuyypxyu".isNice())
        assertEquals(false, "dvszwmarrgswjxmb".isNice())
    }

    @Test
    fun `part 1`() {
        val count = File("./inputs/day05.txt").readLines().count { it.isNice() }
        assertEquals(236, count)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(true, "qjhvhtzxzqqjkmpb".isNice2())
        assertEquals(true, "xxyxx".isNice2())
        assertEquals(false, "aaa".isNice2())
        assertEquals(false, "uurcxstgmygtbstg".isNice2())
        assertEquals(false, "ieodomkazucvgmuy".isNice2())
    }

    @Test
    fun `part 2`() {
        val count = File("./inputs/day05.txt").readLines().count { it.isNice2() }
        assertEquals(69, count)
    }
}