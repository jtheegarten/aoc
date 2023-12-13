package net.sheltem.aoc.y2023

import kotlin.math.min

suspend fun main() {
    Day13().run()
}

class Day13 : Day<Long>(405, 400) {

    override suspend fun part1(input: List<String>): Long = input.joinToString("\n").split("\n\n").sumOf { it.mirrorSum() }

    override suspend fun part2(input: List<String>): Long = input.joinToString("\n").split("\n\n").sumOf { it.mirrorSum(1) }

}
private fun String.mirrorSum(smudgeCount: Int = 0): Long {
    val mirrorMap = this.split("\n")

    val horizontal = horizontalMirrorLine(mirrorMap, smudgeCount)?.let { (it + 1) * 100L }?: 0L
    val vertical = (if (horizontal == 0L) verticalMirrorLine(mirrorMap, smudgeCount) else null)?.let { it + 1L } ?: 0L

    return horizontal + vertical
}

private fun verticalMirrorLine(mirrorMap: List<String>, smudgeCount: Int): Int? {
    val maxSize = mirrorMap.maxOf { it.length }
    val rotatedMap = mutableListOf<String>()

    for (i in 0 until maxSize) {
        val stringBuilder = StringBuilder()
        for (j in mirrorMap.indices.reversed()) {
            if (i < mirrorMap[j].length) {
                stringBuilder.append(mirrorMap[j][i])
            }
        }
        rotatedMap.add(stringBuilder.toString())
    }

    return horizontalMirrorLine(rotatedMap,smudgeCount)
}

private fun horizontalMirrorLine(mirrorMap: List<String>, smudgeCount: Int = 0): Int? = mirrorMap
    .mapIndexedNotNull { index, _ ->
        val up = index < mirrorMap.size / 2

        val halves = when (up) {
            true -> {
                val upperHalf = mirrorMap.take(index + 1)
                val lowerHalf = mirrorMap.subList(index + 1, min(mirrorMap.size, index + 2 + index))
                upperHalf to lowerHalf
            }

            false -> {
                val lowerHalf = mirrorMap.subList(index + 1, mirrorMap.size)
                val upperHalf = mirrorMap.take(index + 1).let { it.drop(it.size - lowerHalf.size) }
                upperHalf to lowerHalf
            }
        }

        val identical = halves.second.reversed().zip(halves.first).sumOf { (stringA, stringB) -> stringA.zip(stringB).count{ (charA, charB) -> charA != charB } } == smudgeCount

        if (index != mirrorMap.size - 1 && identical) index else null
    }.firstOrNull()
