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

        part1(testInput).let { require(it == part1Test) { "Part 1: $it was not correct." } }
        val start1 = Instant.now()
        val part1Result = part1(input).toString()
        val part1Duration = Duration.between(start1, Instant.now())
        println("Part1: $part1Result")
        println("Execution time: $part1Duration\n")

        part2(testInput).let { require(it == part2Test) { "Part 2: $it was not correct." } }
        val start2 = Instant.now()
        val part2Result = part2(input).toString()
        val part2Duration = Duration.between(start2, Instant.now())
        println("Part2: $part2Result")
        println("Execution time: $part2Duration\n")


    }


    abstract fun part1(input: List<String>): T
    abstract fun part2(input: List<String>): T

}
