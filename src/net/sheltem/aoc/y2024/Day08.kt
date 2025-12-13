package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day08().run()
}

class Day08 : Day<Long>(14, 34) {

    override suspend fun part1(input: List<String>): Long = input
        .toGrid<Char>()
        .countAntinodes()

    override suspend fun part2(input: List<String>): Long = input
        .toGrid<Char>()
        .countAntinodes(true)

    private fun Grid<Char>.countAntinodes(repeat: Boolean = false): Long =
        this.allCoordinates.flatMap { coord ->
            if (this[coord] != '.') {
                this.findAntinodes(coord, this[coord]!!, repeat)
            } else {
                emptySet()
            }
        }.filter { this.contains(it) }
            .toSet()
            .size
            .toLong()

    private fun Grid<Char>.findAntinodes(pos: PositionInt, char: Char, repeat: Boolean = false): Set<PositionInt> =
        allCoordinates.flatMap { coord ->
            if (char == this[coord] && pos != coord) {
                val diff = pos - coord

                generateSequence(0) { it + 1 }
                    .map { mult ->
                        setOf(pos + (diff * mult), coord - (diff * mult))
                    }.takeWhile { it.any { newPos -> this.contains(newPos) } }
                    .let {
                        if (repeat) it else it.drop(1).take(1)
                    }.flatten()
                    .toSet()
            } else {
                emptySet()
            }
        }.toSet()
}
