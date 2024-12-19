package org.ricall.day19

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private data class Input(val ruleList: List<Pair<String, String>>, val molecule: String)

private fun parseInput(input: String): Input {
    val parts = input.split("\n\n")
    val ruleList = parts[0].lines()
        .map {
            val (a, b) = it.split(" => ")
            a to b
        }
    val molecule = parts[1].trim()
    return Input(ruleList, molecule)
}

private fun calibrate(input: String): Int {
    val (rules, molecule) = parseInput(input)

    return buildSet<String> {
        rules.forEach { (from, to) ->
            var index = molecule.indexOf(from)
            while (index >= 0) {
                add(molecule.substring(0, index) + to + molecule.substring(index + from.length))
                index = molecule.indexOf(from, index + from.length)
            }
        }
    }.size
}

private fun fabricationSteps(input: String): Int {
    val (ruleList, target) = parseInput(input)

    var rules = ruleList
    var molecule = target
    var steps = 0
    while (molecule != "e") {
        val originalMolecule = molecule
        for (rule in rules) {
            val (from, to) = rule
            val index = molecule.indexOf(to)
            if (index >= 0) {
                molecule = molecule.substring(0, index) + from + molecule.substring(index + to.length)
                steps++
            }
        }
        if (originalMolecule == molecule) {
            molecule = target
            steps = 0
            rules = rules.shuffled() // Random Walk solution
        }
    }
    return steps
}

class Day19 {
    @Test
    fun `part 1 test data`() {
        val result = calibrate("""
            |H => HO
            |H => OH
            |O => HH
            |
            |HOHOHO""".trimMargin()
        )

        assertEquals(7, result)
    }

    @Test
    fun `part 1`() {
        val result = calibrate(File("./inputs/day19.txt").readText())

        assertEquals(576, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = fabricationSteps("""
            |e => H
            |e => O
            |H => HO
            |H => OH
            |O => HH
            |
            |HOH""".trimMargin()
        )

        assertEquals(3, result)
    }

    @Test
    fun `part 2`() {
        val result = fabricationSteps(File("./inputs/day19.txt").readText())

        assertEquals(207, result)
    }
}