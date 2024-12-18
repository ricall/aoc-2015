import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day11 {
    private fun atLeastTwoNonOverlappingPairsOfCharacters(password: List<Char>): Boolean {
        var count = 0
        var nextValidIndex = 0
        for (index in (0..password.size - 2)) {
            if (password[index] == password[index + 1]) {
                if (index >= nextValidIndex) {
                    nextValidIndex = index + 2
                    count++
                }
            }
        }
        return count >= 2
    }

    private fun isInvalidPassword(password: List<Char>) = password.any { it == 'i' || it == 'o' || it == 'l' }
            || password.windowed(3).none { (ch1, ch2, ch3) -> ch2 == ch1 + 1 && ch3 == ch2 + 1 }
            || !atLeastTwoNonOverlappingPairsOfCharacters(password)

    private fun isValidPassword(password: String): Boolean = !isInvalidPassword(password.map { it })

    private fun increment(chars: MutableList<Char>, index: Int) {
        if (index < 0) return
        if (chars[index] == 'z') {
            chars[index] = 'a'
            increment(chars, index - 1)
        } else {
            chars[index] = chars[index] + 1
            return
        }
    }

    private fun nextPassword(password: String): String {
        val chars = password.map { it }.toMutableList()
        val lastIndex = chars.size - 1

        do { increment(chars, lastIndex) } while(isInvalidPassword(chars))
        return chars.joinToString("")
    }

    @Test
    fun `part 1 test data`() {
        assertEquals(false, isValidPassword("hijklmmn"))
        assertEquals(false, isValidPassword("abbceffg"))
        assertEquals(false, isValidPassword("abbcegjk"))
        assertEquals("abcdffaa", nextPassword("abcdefgh"))
        assertEquals("ghjaabcc", nextPassword("ghijklmn"))
    }

    @Test
    fun `part 1`() {
        assertEquals("cqjxxyzz", nextPassword(File("./inputs/day11.txt").readText()))
    }

    @Test
    fun `part 2`() {
        assertEquals("cqkaabcc", nextPassword(nextPassword(File("./inputs/day11.txt").readText())))
    }
}