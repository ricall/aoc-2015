import kotlinx.serialization.json.*
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private val NUMBER_REGEX = Regex("""-?\d+""")

private fun String.parseNumbers(): List<Int> = NUMBER_REGEX.findAll(this).map { it.value.toInt() }.toList()

private fun JsonObject.hasRedValue() =
    this.values.any { it is JsonPrimitive && it.isString && it.content == "red" }

private fun sumNonRedIntegersInJsonObject(element: JsonElement): Int = when (element) {
    is JsonObject -> when (element.hasRedValue()) {
        true -> 0
        else -> element.values.sumOf { sumNonRedIntegersInJsonObject(it) }
    }
    is JsonArray -> element.sumOf { sumNonRedIntegersInJsonObject(it) }
    is JsonPrimitive -> if (element.isString) 0 else element.int
    else -> throw Exception("Unknown element type: ${element::class.simpleName}")
}
private fun String.sumNonRedNumbers() = sumNonRedIntegersInJsonObject(Json.parseToJsonElement(this))

class Day12 {
    @Test
    fun `part 1 test data`() {
        assertEquals(6, "[1,2,3]".parseNumbers().sum())
        assertEquals(6, "{\"a\":2,\"b\":4}".parseNumbers().sum())
        assertEquals(3, "[[[3]]]".parseNumbers().sum())
        assertEquals(3, "{\"a\":{\"b\":4},\"c\":-1}".parseNumbers().sum())
        assertEquals(0, "{\"a\":[-1,1]}".parseNumbers().sum())
        assertEquals(0, "[-1,{\"a\":1}]".parseNumbers().sum())
        assertEquals(0, "[]".parseNumbers().sum())
        assertEquals(0, "{}".parseNumbers().sum())
    }

    @Test
    fun `part 1`() {
        assertEquals(111754, File("./inputs/day12.txt").readText().parseNumbers().sum())
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(6, "[1,2,3]".sumNonRedNumbers())
        assertEquals(4, "[1,{\"c\":\"red\",\"b\":2},3]".sumNonRedNumbers())
        assertEquals(0, "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}".sumNonRedNumbers())
        assertEquals(6, "[1,\"red\",5]".sumNonRedNumbers())
    }

    @Test
    fun `part 2`() {
        assertEquals(65402, File("./inputs/day12.txt").readText().sumNonRedNumbers())
    }
}