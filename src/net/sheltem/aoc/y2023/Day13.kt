package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.rotateCockwise

suspend fun main() {
    Day13().run()
}

class Day13 : Day<Long>(405, 400) {

    override suspend fun part1(input: List<String>): Long = input.joinToString("\n").split("\n\n").sumOf { it.mirrorSum() }

    override suspend fun part2(input: List<String>): Long = input.joinToString("\n").split("\n\n").sumOf { it.mirrorSum(1) }

}

private fun String.mirrorSum(smudgeCount: Int = 0): Long {
    val mirrorMap = this.split("\n")

    val horizontal = horizontalMirrorLine(mirrorMap, smudgeCount)?.let { (it + 1) * 100L } ?: 0L
    val vertical = (if (horizontal == 0L) verticalMirrorLine(mirrorMap, smudgeCount) else null)?.let { it + 1L } ?: 0L

    return horizontal + vertical
}

private fun verticalMirrorLine(mirrorMap: List<String>, smudgeCount: Int): Int? = horizontalMirrorLine(mirrorMap.rotateCockwise(), smudgeCount)

private fun horizontalMirrorLine(mirrorMap: List<String>, smudgeCount: Int = 0): Int? = mirrorMap
    .mapIndexedNotNull { index, _ ->

        val upper = mirrorMap.take(index + 1)
        val lower = mirrorMap.drop(upper.size)
        val halves = if (upper.size >= lower.size) {
            upper.drop(upper.size - lower.size) to lower
        } else {
            upper to lower.dropLast(lower.size - upper.size)
        }

        val identical = halves.second.reversed().zip(halves.first).sumOf { (stringA, stringB) -> stringA.zip(stringB).count { (charA, charB) -> charA != charB } } == smudgeCount

        if (index != mirrorMap.size - 1 && identical) index else null
    }.firstOrNull()
