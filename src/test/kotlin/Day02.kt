import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day02 {
    private fun String.wrappingPaperSize(): Int {
        val (l, w, h) = this.split("x").map(String::toInt)
        val parts = listOf(l*w, w*h, h*l)
        return parts.sumOf { 2 * it } + parts.min()
    }

    private fun String.ribbonLength(): Int {
        val parts = this.split("x").map(String::toInt)
        val bow = parts.fold(1) { acc, i -> acc * i }
        val (s1, s2) = parts.sorted()
        return s1*2 + s2*2 + bow
    }

    @Test
    fun `part 1 test data`() {
        assertEquals(58, "2x3x4".wrappingPaperSize())
        assertEquals(43, "1x1x10".wrappingPaperSize())
    }

    @Test
    fun `part 1`() {
        val result = File("./inputs/day02.txt").readLines().sumOf { it.wrappingPaperSize() }
        assertEquals(1586300, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(34, "2x3x4".ribbonLength())
        assertEquals(14, "1x1x10".ribbonLength())
    }

    @Test
    fun `part 2`() {
        val result = File("./inputs/day02.txt").readLines().sumOf { it.ribbonLength() }
        assertEquals(3737498, result)
    }
}