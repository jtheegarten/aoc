package net.sheltem.aoc.common

import java.io.File
import java.time.Duration
import java.time.Instant

abstract class Day<T>(
    private val part1Test: T,
    private val part2Test: T,
) {

    fun run(skipTest: Boolean = false) {

        val number = this::class.java.simpleName.takeLast(2)

        val testInput = File("src/net.sheltem.aoc.common.Day${number}_test.txt").readLines()
        val input = File("src/net.sheltem.aoc.common.Day${number}.txt").readLines()

        println("=== net.sheltem.aoc.common.Day $number: ===\n")

        listOf(part1Test, part2Test).forEachIndexed { i, it ->
            println("Part${i+1}: ")
            runPart(i+1, it, testInput, input, skipTest)
        }

    }

    private fun runPart(part: Int, testResult: T, testInput: List<String>, input: List<String>, skipTest: Boolean) {


        val list = if (skipTest) listOf("real" to input) else listOf("Test" to testInput, "Real" to input)

        list.forEach { (step, input) ->
            val start = Instant.now()
            val partResult = (if (part == 1) ::part1 else ::part2).invoke(input)
            if (step == "Test") require(partResult == testResult) { "Result $partResult is not correct, expecting $testResult" }
            val partDuration = Duration.between(start, Instant.now())
            println("${step}: $partResult\n  in $partDuration")
        }

        println()
    }

    abstract fun part1(input: List<String>): T
    abstract fun part2(input: List<String>): T

}
