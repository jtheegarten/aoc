package net.sheltem.common

import java.io.File
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

abstract class Day<T>(
    private val year: Int,
    private val part1Test: T,
    private val part2Test: T,
) {

    suspend fun run(skipTest: Boolean = false) {

        val number = this::class.java.simpleName.takeLast(2)

        val testInput = File("src/net/sheltem/aoc/y$year/data/Day${number}_test.txt").readLines()
        val test2Input = File("src/net/sheltem/aoc/y$year/data/Day${number}_2_test.txt").let {
            if (it.exists())
                it.readLines()
            else
                testInput
        }
        val input = File("src/net/sheltem/aoc/y$year/data/Day${number}.txt").readLines()

        val total = Instant.now()

        println("=== $year Day $number: ===\n")

        listOf(part1Test, part2Test).forEachIndexed { i, it ->
            println("Part${i+1}: ")
            runPart(i+1, it, testInput, test2Input, input, skipTest)
        }

        println("Total time: ${Duration.between(total, Instant.now()).toNanos().toDouble() / 1_000_000} ms")
    }

    private suspend fun runPart(part: Int, testResult: T, testInput: List<String>, test2Input: List<String>, input: List<String>, skipTest: Boolean) {

        val data = if (skipTest) mutableMapOf("Real" to input) else mutableMapOf("Test" to testInput, "Real" to input)
        if (part == 2 && !skipTest) data["Test"] = test2Input

        data.entries.forEach { (step, input) ->
            val start = Instant.now()
            val partResult = (if (part == 1) ::part1 else ::part2).invoke(input)
            val partDuration = start.until(Instant.now(), ChronoUnit.NANOS).toDouble() / 1_000_000
            val testInfo = if (step == "Test" && partResult != testResult) " <- incorrect, expecting $testResult" else ""
            println("${step}: $partResult$testInfo\n  in $partDuration ms")
        }

        println()
    }

    abstract suspend fun part1(input: List<String>): T
    abstract suspend fun part2(input: List<String>): T

}
