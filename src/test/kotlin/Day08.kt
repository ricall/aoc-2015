package org.ricall.day08

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun countDecodedSizeDifference(line: String): Int {
    val length = line.length
    val text = line
        .lowercase()
        .replace("\\\\", "X")
        .replace("\\\"", "X")
        .replace("\\\\x[0-9a-f]{2}".toRegex(), "X")
    return length - text.length + 2
}

private fun decodeInput(input: String) = input.lines().map(::countDecodedSizeDifference).sum()

private fun countEncodedSizeDifference(line: String): Int {
    val length = line.length
    val text = line
        .lowercase()
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
    return text.length + 2 - length
}

private fun encodeInput(input: String) = input.lines().map(::countEncodedSizeDifference).sum()

class Day08 {
    private val TEST_DATA = """
        |""
        |"abc"
        |"aaa\"aaa"
        |"\x27"""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = decodeInput(TEST_DATA)
        assertEquals(12, result)
    }

    @Test
    fun `part 1`() {
        val result = decodeInput(File("./inputs/day08.txt").readText())
        assertEquals(1371, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = encodeInput(TEST_DATA)
        assertEquals(19, result)
    }

    @Test
    fun `part 2`() {
        val result = encodeInput(File("./inputs/day08.txt").readText())
        assertEquals(2117, result)
    }
}