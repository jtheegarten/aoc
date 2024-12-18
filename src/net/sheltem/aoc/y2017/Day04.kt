package net.sheltem.aoc.y2017

suspend fun main() {
    Day04().run()
}

class Day04 : Day<Long>(2, 2) {

    override suspend fun part1(input: List<String>): Long = input.map { it.split(" ") }.count { it.toSet().size == it.size }.toLong()

    override suspend fun part2(input: List<String>): Long = input.map { it.split(" ") }.map { it.sortStrings() }.count { it.toSet().size == it.size }.toLong()

    private fun List<String>.sortStrings() = map { it.toCharArray().sorted().joinToString("") }
}
