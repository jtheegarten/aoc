import java.io.File

abstract class Day<T>(
    private val part1Test: T,
    private val part2Test: T,
) {

    fun run() {

        val number = this::class.java.simpleName.takeLast(2)

        val testInput = File("src/Day${number}_test.txt").readLines()
        val input = File("src/Day${number}.txt").readLines()

        part1(testInput).let { require(it == part1Test) { "Part 1: $it was not correct." } }
        println("Part 1: ${part1(input).toString()}")

        part2(testInput).let { require(it == part2Test) { "Part 2: $it was not correct." } }
        println("Part 2: ${part2(input).toString()}")

    }


    abstract fun part1(input: List<String>): T
    abstract fun part2(input: List<String>): T

}
