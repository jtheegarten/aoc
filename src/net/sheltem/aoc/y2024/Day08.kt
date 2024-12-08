package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day08().run()
}

class Day08 : Day<Long>(14, 34) {

    override suspend fun part1(input: List<String>): Long = input
        .countAntinodes()

    override suspend fun part2(input: List<String>): Long = input
        .countAntinodes(true)
}

private fun List<String>.countAntinodes(repeat: Boolean = false): Long =
    this.flatMapIndexed { y, row ->
        row.flatMapIndexed { x, char ->
            if (this.charAt(x to y) != '.') {
                this.findAntinodes(x to y, char, repeat)
            } else {
                emptySet()
            }
        }
    }.filter { it.within(this) }
        .toSet()
        .size
        .toLong()

private fun List<String>.findAntinodes(pos: PositionInt, char: Char, repeat: Boolean = false): Set<PositionInt> =
    this.flatMapIndexed { y, row ->
        row.flatMapIndexed { x, innerChar ->
            if (char == innerChar && pos != x to y) {
                val diff = pos - (x to y)

                generateSequence(0) { it + 1 }
                    .map { mult ->
                        setOf(pos + (diff * mult), (x to y) - (diff * mult))
                    }.takeWhile { it.any { newPos -> newPos.within(this) } }
                    .let {
                        if (repeat) it else it.drop(1).take(1)
                    }.flatten()
                    .toSet()
            } else {
                emptySet()
            }
        }
    }.toSet()
