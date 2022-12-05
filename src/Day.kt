import java.io.File

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
    }


    abstract fun part1(input: List<String>): T
    abstract fun part2(input: List<String>): T

}
