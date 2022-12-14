fun main() {
    Day04().run()
}

class Day04: Day<Int>(2, 4) {

    override fun part1(input: List<String>): Int = input.count { it.fullyContained() }

    override fun part2(input: List<String>): Int = input.count { it.overlap() }
}

fun String.fullyContained() = split(",").map { it.toNumberList() }
    .let { (first, second) -> first.containsAll(second) || second.containsAll(first) }

fun String.overlap() = split(",").map { it.toNumberList().toSet() }.let { (numbers1, numbers2) -> numbers1.intersect(numbers2).isNotEmpty() }

fun String.toNumberList() = split("-").let { (start, end) -> start.toLong()..end.toLong() }.toList()
