import org.junit.jupiter.api.Test
import org.ricall.utils.permutations
import java.io.File
import kotlin.test.assertEquals

private val PARSE_REGEX = Regex("""^(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+).$""")

private typealias Preferences = Map<String, Map<String, Int>>
private typealias Seating = List<String>

class Day13 {
    private val TEST_DATA = """
        |Alice would gain 54 happiness units by sitting next to Bob.
        |Alice would lose 79 happiness units by sitting next to Carol.
        |Alice would lose 2 happiness units by sitting next to David.
        |Bob would gain 83 happiness units by sitting next to Alice.
        |Bob would lose 7 happiness units by sitting next to Carol.
        |Bob would lose 63 happiness units by sitting next to David.
        |Carol would lose 62 happiness units by sitting next to Alice.
        |Carol would gain 60 happiness units by sitting next to Bob.
        |Carol would gain 55 happiness units by sitting next to David.
        |David would gain 46 happiness units by sitting next to Alice.
        |David would lose 7 happiness units by sitting next to Bob.
        |David would gain 41 happiness units by sitting next to Carol.""".trimMargin()

    private fun parsePreferences(input: String) = buildMap<String, MutableMap<String, Int>> {
        input.lines().forEach { line ->
            val (name, gainLose, happiness, otherName) = PARSE_REGEX.matchEntire(line)!!.destructured
            getOrPut(name) { mutableMapOf() }[otherName] = happiness.toInt() * (if (gainLose == "gain") 1 else -1)
        }
    }

    private fun maximumHappiness(preferences: Preferences, seating: Seating) =
        (seating + seating.first()).zipWithNext { a, b ->
            (preferences.get(a)?.get(b) ?: 0) + (preferences.get(b)?.get(a) ?: 0)
        }.sum()

    private fun optimalSeatingHappiness(input: String, additional: Seating = emptyList()): Int {
        val preferences = parsePreferences(input)
        val people = preferences.keys.toList() + additional
        val permutations = people.permutations(people.size)

        return permutations.maxOf { maximumHappiness(preferences, it) }
    }

    @Test
    fun `part 1 test data`() {
        val result = optimalSeatingHappiness(TEST_DATA)

        assertEquals(330, result)
    }

    @Test
    fun `part 1`() {
        val result = optimalSeatingHappiness(File("./inputs/day13.txt").readText())

        assertEquals(618, result)
    }

    @Test
    fun `part 2`() {
        val result = optimalSeatingHappiness(File("./inputs/day13.txt").readText(), listOf("me"))

        assertEquals(601, result)
    }
}