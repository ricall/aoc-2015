import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private val NUMBER_REGEX = Regex("^[0-9]+$")
private val ASSIGN_REGEX = Regex("^([a-z0-9]+) -> ([a-z]+)$")
private val AND_REGEX = Regex("^([a-z0-9]+) AND ([a-z0-9]+) -> ([a-z]+)$")
private val OR_REGEX = Regex("^([a-z0-9]+) OR ([a-z0-9]+) -> ([a-z]+)$")
private val LEFT_SHIFT_REGEX = Regex("^([a-z0-9]+) LSHIFT ([0-9]+) -> ([a-z]+)$")
private val RIGHT_SHIFT_REGEX = Regex("^([a-z0-9]+) RSHIFT ([0-9]+) -> ([a-z]+)$")
private val NOT_REGEX = Regex("^NOT ([a-z0-9]+) -> ([a-z]+)$")

private class LogicGates(input: String) {
    private val circuit = mutableMapOf<String, Gate>()
    private val cachedSignals = mutableMapOf<String, UShort>()

    fun getSignal(name: String) = cachedSignals.getOrPut(name) {
        NUMBER_REGEX.find(name)?.let { return@getOrPut it.value.toUShort() }
        circuit[name]?.value() ?: throw Exception("Signal $name not found")
    }

    fun copySignal(signals: Pair<String, String>) {
        val (from, to) = signals
        val value = getSignal(from)
        cachedSignals.clear()
        cachedSignals[to] = value
    }

    private fun interface Gate { fun value(): UShort }

    init {
        val signalProcessingRules = mapOf<Regex, (List<String>) -> Unit>(
            ASSIGN_REGEX to { circuit[it[2]] = Gate { getSignal(it[1]) } },
            AND_REGEX to { circuit[it[3]] = Gate { getSignal(it[1]) and getSignal(it[2]) } },
            OR_REGEX to { circuit[it[3]] = Gate { getSignal(it[1]) or getSignal(it[2]) } },
            LEFT_SHIFT_REGEX to {
                circuit[it[3]] =
                    Gate { (getSignal(it[1]).toInt() shl getSignal(it[2]).toInt()).toUShort() }
            },
            RIGHT_SHIFT_REGEX to {
                circuit[it[3]] =
                    Gate { (getSignal(it[1]).toInt() shr getSignal(it[2]).toInt()).toUShort() }
            },
            NOT_REGEX to { circuit[it[2]] = Gate { getSignal(it[1]).inv() } }
        )

        input.lines().forEach { line ->
            signalProcessingRules.forEach { (regex, rule) ->
                regex.find(line)?.let { rule(it.groupValues) }
            }
        }
    }
}

class Day07 {
    private val TEST_DATA = """
        |123 -> x
        |456 -> y
        |x AND y -> d
        |x OR y -> e
        |x LSHIFT 2 -> f
        |y RSHIFT 2 -> g
        |NOT x -> h
        |NOT y -> i""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val gates = LogicGates(TEST_DATA)

        listOf(
            "d" to 72,
            "e" to 507,
            "f" to 492,
            "g" to 114,
            "h" to 65412,
            "i" to 65079,
            "x" to 123,
            "y" to 456
        ).forEach { (register, value) ->
            assertEquals(value.toUShort(), gates.getSignal(register))
        }
    }

    @Test
    fun `part 1`() {
        val gates = LogicGates(File("./inputs/day07.txt").readText())

        assertEquals(16076.toUShort(), gates.getSignal("a"))
    }

    @Test
    fun `part 2`() {
        val gates = LogicGates(File("./inputs/day07.txt").readText())
        gates.copySignal("a" to "b")

        assertEquals(2797.toUShort(), gates.getSignal("a"))
    }
}