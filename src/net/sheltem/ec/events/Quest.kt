package net.sheltem.ec.events

import java.io.File
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

abstract class Quest<T>(
    private val event: String,
    private val testResult: List<T>,
) {
    suspend fun run(skipTest: Boolean = false) {

        val number = this::class.java.simpleName.takeLast(2)

        val testInput = (1..3).map { File("src/net/sheltem/ec/events/$event/data/Quest${number}_test$it.txt").readLines() }
        val input = (1..3).map { File("src/net/sheltem/ec/events/$event/data/Quest$number-$it.txt").readLines() }

        val total = Instant.now()

        println("=== $event Quest $number: ===\n")

        val outputs = testResult.mapIndexed { i, it ->
            runPart(i + 1, it, testInput[i], input[i], skipTest).output()
        }

        (0..5).map { i ->
            outputs.map { it[i] }
                .joinToString("") { it.padEnd(30) }
        }.forEach { println(it) }

        println("\nTotal time: ${Duration.between(total, Instant.now()).toNanos().toDouble() / 1_000_000} ms")
    }

    private suspend fun runPart(part: Int, testExpect: T, testInput: List<String>, input: List<String>, skipTest: Boolean): ExecutionResult<T> {
        val partFunction = when (part) {
            1 -> ::part1
            2 -> ::part2
            3 -> ::part3
            else -> error("Unknown part: $part")
        }

        val testStart = Instant.now()
        val testResult = if (skipTest) null else partFunction.invoke(testInput)
        val testDuration = testStart.until(Instant.now(), ChronoUnit.NANOS).toDouble() / 1_000_000

        val start = Instant.now()
        val result = partFunction.invoke(input)
        val duration = start.until(Instant.now(), ChronoUnit.NANOS).toDouble() / 1_000_000

        return ExecutionResult(
            part,
            result,
            testResult,
            duration,
            testDuration,
            testExpect
        )
    }

    abstract suspend fun part1(input: List<String>): T
    abstract suspend fun part2(input: List<String>): T
    abstract suspend fun part3(input: List<String>): T

    data class ExecutionResult<T>(
        val part: Int,
        val result: T,
        val testResult: T?,
        val duration: Double,
        val testDuration: Double,
        val expected: T,
    ) {
        val testSuccess = testResult?.let { it == expected }

        fun output(): List<String> {
            return listOf(
                "Part$part:",
                "Test: $testResult" + if (testSuccess == false) " != $expected" else "",
                "  in $testDuration ms",
                "",
                "Real: $result",
                "  in $duration ms",
            )
        }
    }
}

