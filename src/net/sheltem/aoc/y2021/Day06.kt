package net.sheltem.aoc.y2021

suspend fun main() {
    Day06().run()
}

class Day06 : Day<Long>(5934, 26984457539) {

    override suspend fun part1(input: List<String>): Long = input[0]
        .toFishMap()
        .let { spawn(80, it) }

    override suspend fun part2(input: List<String>): Long = input[0]
        .toFishMap()
        .let { spawn(256, it) }
}

private fun String.toFishMap() = split(",")
    .map { it.toInt() }
    .groupingBy { it }
    .eachCount()
    .map { it.key to it.value.toLong() }
    .toMap()

private fun spawn(repeats: Int, startMap: Map<Int, Long>): Long {
    var resultMap = startMap

    repeat(repeats) { _ ->
        resultMap = mapOf(8 to (resultMap[0] ?: 0)) + resultMap.map {
            when (it.key) {
                0 -> 6 to (it.value + (resultMap[7] ?: 0))
                in 1..8 -> it.key - 1 to it.value
                else -> throw IllegalStateException()
            }
        }
    }
    return resultMap.values.sum()

}
