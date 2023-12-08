package net.sheltem.aoc.y2015

suspend fun main() {
    Day12().run()
}

class Day12 : Day<Long>(0, 0) {
    override suspend fun part1(input: List<String>): Long = input[0].numbers().sum()

    override suspend fun part2(input: List<String>): Long = input[0].replace(Regex("(\\{[^{]*\"red\"[^}]*})"), "").also { println(it) }.numbers().sum()
}

private fun String.numbers() = Regex("(-*\\d+)").findAll(this).map { it.value.toLong() }.toList()
