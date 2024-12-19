import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun getDivisors(n: Int): Set<Int> {
    val smallDivisors = (1..Math.sqrt(n.toDouble()).toInt() + 1).filter { n % it == 0 }
    val largeDivisors = smallDivisors.filter { n != (it * it) }.map { n / it }

    return (smallDivisors + largeDivisors).toSet()
}

private fun part1() = generateSequence(1) { it + 1 }.map { house -> getDivisors(house).sum() * 10 }

private fun part2() = generateSequence(1) { it + 1 }
    .map { house -> getDivisors(house).filter { house / it <= 50 }.sum() * 11 }

private fun findHouseNumber(target: Long, presents: Sequence<Int>): Int {
    presents.forEachIndexed { index, presentCount ->
        if (presentCount >= target) {
            return index + 1
        }
    }
    return -1
}

class Day20 {
    @Test
    fun `part 1 test data`() {
        val firstNinePresents = part1().take(9)

        assertEquals(
            listOf(
                10,
                30,
                40,
                70,
                60,
                120,
                80,
                150,
                130
            ), firstNinePresents.toList()
        )
    }

    @Test
    fun `part 1`() {
        val result = findHouseNumber(File("./inputs/day20.txt").readText().toLong(), part1())

        assertEquals(776160, result)
    }

    @Test
    fun `part 2`() {
        val result = findHouseNumber(File("./inputs/day20.txt").readText().toLong(), part2())

        assertEquals(786240, result)
    }
}