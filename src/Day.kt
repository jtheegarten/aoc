import java.io.File

abstract class Day<T>(
    private val part1Test: T,
    private val part2Test: T,
) {

    fun run() {

        val number = this::class.java.simpleName.takeLast(2)

        val testInput = File("src/Day${number}_test.txt").readLines()
        val input = File("src/Day${number}.txt").readLines()

        require(part1(testInput) == part1Test)
        println("Part 1: ${part1(input).toString()}")

        require(part2(testInput) == part2Test)
        println("Part 2: ${part2(input).toString()}")

    }


    abstract fun part1(input: List<String>): T
    abstract fun part2(input: List<String>): T

}
