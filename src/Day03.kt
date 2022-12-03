import java.io.File

fun main() {

    fun part1(input: List<String>): Int =
        input.flatMap { it.duplicates() }.sumOf { it.score() }

    fun part2(input: List<String>): Int =
        input.chunked(3).flatMap { it.badge() }.sumOf { it.score() }

    val input = File("src/Day03.txt").readLines()
//    require(part1(input) == 157)
//    require(part2(input) == 70)

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")

}

fun List<String>.badge() = map { it.toSet() }.reduce(Set<Char>::intersect)

fun String.duplicates() =
    chunked(size = (length / 2)).map { it.toSet() }.reduce(Set<Char>::intersect)

fun Char.score() = valueMap[this] ?: 0

val valueMap = (('a'..'z').plus('A'..'Z')).zip(1..52).toMap()
