package net.sheltem.aoc.y2015

suspend fun main() {
    Day02().run()
}

class Day02 : Day<Int>(58, 34) {
    override suspend fun part1(input: List<String>): Int = input.map { it.toCuboid() }.sumOf { it.surface + it.slack }

    override suspend fun part2(input: List<String>): Int = input.map { it.toCuboid() }.sumOf { it.perimeter + it.volume }
}

private fun String.toCuboid() = split("x").map { it.toInt() }.let { (l, w, h) -> Cuboid(l, w, h) }

private data class Cuboid(val length: Int, val width: Int, val height: Int) {
    val sides = listOf(length * width, width * height, height * length)
    val surface = sides.sumOf{ it * 2 }
    val slack = sides.min()
    val perimeter = listOf(length, width, height).sorted().take(2).sumOf { it * 2 }
    val volume = length * width * height
}
