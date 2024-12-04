package net.sheltem.aoc.y2024

import net.sheltem.common.Direction8
import net.sheltem.common.lineTo
import net.sheltem.common.move
import net.sheltem.common.takeWord

suspend fun main() {
    Day04().run()
}

class Day04 : Day<Long>(18, 9) {

    override suspend fun part1(input: List<String>): Long = input.countXmas()

    override suspend fun part2(input: List<String>): Long = input.countXmas(true)
}

private fun List<String>.countXmas(crossed: Boolean = false): Long {
    var xmas = 0L
    for (y in this.indices) {
        for (x in this[0].indices) {
            if (!crossed && this[y][x] == 'X') {
                xmas += this.findXmas(x, y)
            }
            if (crossed && this[y][x] == 'A') {
                xmas += this.findXedMas(x, y)
            }
        }
    }

    return xmas
}

private fun List<String>.findXmas(x: Int, y: Int): Long {
    var xmas = 0L

    for (dir in Direction8.entries) {
        if ((x to y).lineTo(dir, 3).takeWord(this) == "XMAS") xmas++
    }

    return xmas
}

private fun List<String>.findXedMas(x: Int, y: Int): Long {
    val word1 = (x to y).move(Direction8.NORTH_WEST).lineTo(Direction8.SOUTH_EAST, 2).takeWord(this)
    val word2 = (x to y).move(Direction8.SOUTH_WEST).lineTo(Direction8.NORTH_EAST, 2).takeWord(this)

    return if (isMAS(listOf(word1, word2))) {
        1
    } else {
        0
    }
}

private fun isMAS(words: List<String>) : Boolean = words.map { it.reversed() }.plus(words).count { it == "MAS" } == 2
