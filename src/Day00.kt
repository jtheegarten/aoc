import java.io.File

const val day = "00"

fun main() {

    fun part1(input: List<String>): Int {
        TODO()
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    val testInput = File("src/Day${day}_test.txt").readLines()
    require(part1(testInput) == 0)
    require(part2(testInput) == 0)

    val input = File("src/Day${day}.txt").readLines()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
