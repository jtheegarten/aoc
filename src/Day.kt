import java.io.File
import java.time.Duration
import java.time.Instant

abstract class Day<T>(
    private val number: String,
    private val part1Test: T,
    private val part2Test: T,
) {

    fun run() {
        val testInput = File("src/Day${number}_test.txt").readLines()
        require(part1(testInput) == part1Test)
        require(part2(testInput) == part2Test)

        val input = File("src/Day${number}.txt").readLines()
        println("Part 1: ${part1(input).toString()}")
        println("Part 2: ${part2(input).toString()}")

        val part1Start = Instant.now()
        repeat(1000) {
            part1(input)
        }
        println("Part 1 avg. execution time: ${Duration.between(part1Start, Instant.now()).dividedBy(1000)}")

        val part2Start = Instant.now()
        repeat(1000) {
            part1(input)
        }
        println("Part 2 avg. execution time: ${Duration.between(part2Start, Instant.now()).dividedBy(1000)}")
    }


    abstract fun part1(input: List<String>): T
    abstract fun part2(input: List<String>): T

}
