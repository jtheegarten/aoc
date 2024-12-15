package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day04().run()
}

class Day04 : Day<Long>(18, 9) {

    override suspend fun part1(input: List<String>): Long = input.toGrid().countXmas()

    override suspend fun part2(input: List<String>): Long = input.toGrid().countXmas(true)

    private fun Grid<Char>.countXmas(crossed: Boolean = false): Long =
        this.allCoordinates.sumOf { pos ->
        when {
            !crossed && this[pos] == 'X' -> this.findXmas(pos)
            crossed && this[pos] == 'A' -> this.findXedMas(pos)
            else -> 0
        }
    }

    private fun Grid<Char>.findXmas(pos: PositionInt): Long =
        Direction8
            .entries
            .count { pos.lineTo(it, 3).takeWord(this) == "XMAS" }
            .toLong()

    private fun Grid<Char>.findXedMas(pos: PositionInt): Long {
        val word1 = pos.move(Direction8.NORTH_WEST).lineTo(Direction8.SOUTH_EAST, 2).takeWord(this)
        val word2 = pos.move(Direction8.SOUTH_WEST).lineTo(Direction8.NORTH_EAST, 2).takeWord(this)

        return if (isMAS(listOf(word1, word2))) {
            1
        } else {
            0
        }
    }

    private fun isMAS(words: List<String>): Boolean = words.map { it.reversed() }.plus(words).count { it == "MAS" } == 2
}
