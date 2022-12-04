import java.io.File

fun main() {

    fun part1(input: List<String>): Int = input.count { it.fullyContained() }

    fun part2(input: List<String>): Int = input.count { it.overlap() }

    val input = File("src/Day04.txt").readLines()
//    require(part1(input) == 2)
//    require(part2(input) == 4)

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun String.fullyContained() = split(",").map { it.toNumberList() }
    .let { it.component1().containsAll(it.component2()) || it.component2().containsAll(it.component1()) }

fun String.overlap() = split(",").map { it.toNumberList().toSet() }.let { it.component1().intersect(it.component2()).isNotEmpty() }

fun String.toNumberList() = split("-").let { it.component1().toLong()..it.component2().toLong() }.toList()
