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
    .let { (first, second) -> first.containsAll(second) || second.containsAll(first) }

fun String.overlap() = split(",").map { it.toNumberList().toSet() }.let { (numbers1, numbers2) -> numbers1.intersect(numbers2).isNotEmpty() }

fun String.toNumberList() = split("-").let { (start, end) -> start.toLong()..end.toLong() }.toList()
