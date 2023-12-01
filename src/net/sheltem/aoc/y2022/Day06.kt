package net.sheltem.aoc.y2022

suspend fun main() {
    Day06().run()
}

class Day06 : Day<Int>(7, 19) {

    override suspend fun part1(input: List<String>): Int = input.first().indexOfMarker()

    override suspend fun part2(input: List<String>): Int = input.first().indexOfMarker(14)
}

private fun String.indexOfMarker(length: Int = 4) = windowed(length, 1).indexOfFirst { it.toSet().size == length } + length
