import kotlinx.cinterop.CValuesRef
import net.sheltem.aoc.readLines
import platform.posix.FILE
import platform.posix.fopen
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

abstract class Day<T>(
    private val part1Test: T,
    private val part2Test: T,
) {

    @OptIn(ExperimentalTime::class)
    fun run() {

        val number = this::class.simpleName?.takeLast(2)

        val testInputFile = fopen("Day${number}_test.txt", "r") ?: throw IllegalArgumentException("Cannot open test input for Day $number")
        val inputFile = fopen("Day${number}.txt", "r") ?: throw IllegalArgumentException("Cannot open input for Day $number")

        val testInput = (testInputFile as CValuesRef<FILE>).readLines()
        val input = (inputFile as CValuesRef<FILE>).readLines()

        println("=== Day $number: ===\n")

        part1(testInput).let { require(it == part1Test) { "Part 1: $it was not correct." } }
        val part1Duration = measureTime {
            val part1Result = part1(input).toString()
            println("Part1: $part1Result")
        }

        println("Execution time: $part1Duration\n")

        part2(testInput).let { require(it == part2Test) { "Part 2: $it was not correct." } }
        val part2Duration = measureTime {
            val part2Result = part2(input).toString()
            println("Part2: $part2Result")
        }
        println("Execution time: $part2Duration\n")


    }


    abstract fun part1(input: List<String>): T
    abstract fun part2(input: List<String>): T

}
