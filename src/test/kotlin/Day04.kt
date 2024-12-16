import org.junit.jupiter.api.Test
import java.io.File
import java.security.MessageDigest
import kotlin.test.assertEquals

class Day04 {
    val md = MessageDigest.getInstance("MD5")

    @OptIn(ExperimentalStdlibApi::class)
    private fun md5(text: String) = md.digest(text.toByteArray()).toHexString()

    private fun String.mine5(): Long = generateSequence(0L) { it + 1 }
        .first { md5("${this}${it}").startsWith("00000") }

    private fun String.mine6(): Long = generateSequence(0L) { it + 1 }
        .first { md5("${this}${it}").startsWith("000000") }


    @Test
    fun `part 1 test data`() {
        assertEquals("000001dbbfa3a5c83a2d506429c7b00e", md5("abcdef609043"))
        assertEquals(609043, "abcdef".mine5())
    }

    @Test
    fun `part 1`() {
        assertEquals(282749, File("./inputs/day04.txt").readText().mine5())
    }

    @Test
    fun `part 2`() {
        assertEquals(9962624, File("./inputs/day04.txt").readText().mine6())
    }
}