import java.io.File
import java.time.Duration
import java.time.Instant

abstract class Day<T>(
    private val part1Test: T,
    private val part2Test: T,
) {

    fun run() {

        val number = this::class.java.simpleName.takeLast(2)

        val testInput = File("src/Day${number}_test.txt").readLines()
        val input = File("src/Day${number}.txt").readLines()

        println("=== Day $number: ===\n")

        listOf(1 to part1Test, 2 to part2Test).forEach {
            runPart(it.first, it.second, testInput, input)
        }

    }

    private fun runPart(part: Int, testResult: T, testInput: List<String>, input: List<String>) {
        println("Part$part: ")

        listOf("Test" to testInput, "Real" to input).forEach { (step, input) ->
            val start = Instant.now()
            val partResult = (if (part == 1) part1(input) else part2(input)).toString()
            if (step == "Test") require(partResult == testResult.toString()) { "Result $partResult is not correct, expecting $testResult" }
            val partDuration = Duration.between(start, Instant.now())
            println("${step}: $partResult\n  in $partDuration")
        }

        println()
    }


    abstract fun part1(input: List<String>): T
    abstract fun part2(input: List<String>): T

}
