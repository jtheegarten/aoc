package net.sheltem.aoc.y2022

fun main() {
    Day03().run()
}

class Day03: Day<Int>(157, 70) {
    override fun part1(input: List<String>): Int =
        input.flatMap { it.duplicates() }.sumOf { it.score() }

    override fun part2(input: List<String>): Int =
        input.chunked(3).flatMap { it.badge() }.sumOf { it.score() }
}

private fun List<String>.badge() = map { it.toSet() }.reduce(Set<Char>::intersect)

private fun String.duplicates() = chunked(size = (length / 2)).map { it.toSet() }.reduce(Set<Char>::intersect)

private fun Char.score() = valueMap[this] ?: 0

private val valueMap = (('a'..'z').plus('A'..'Z')).zip(1..52).toMap()
