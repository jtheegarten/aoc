package net.sheltem.aoc.y2015

fun main() {
    Day05().run()
}

class Day05 : Day<Int>(1, 0) {
    override fun part1(input: List<String>): Int = input.count { it.isNice() }

    override fun part2(input: List<String>): Int = input.count { it.isRealNice() }
}

private val vowels = listOf('a', 'e', 'i', 'o', 'u')
private val banned = listOf("ab", "cd", "pq", "xy")

private fun String.isNice(): Boolean =
    this.count { vowels.contains(it) } >= 3 &&
    this.zipWithNext().any{ it.first == it.second } &&
    banned.none { this.contains(it) }

private fun String.isRealNice(): Boolean =
    this.windowed(3).any { it[0] == it[2] } && this.windowed(2).mapIndexed {
            i, s -> this.lastIndexOf(s) -i > 1
    }.any { it }
