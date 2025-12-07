package net.sheltem.aoc.y2025

import net.sheltem.common.Direction
import net.sheltem.common.Direction.EAST
import net.sheltem.common.Direction.WEST
import net.sheltem.common.Grid
import net.sheltem.common.PositionInt
import net.sheltem.common.move
import net.sheltem.common.toGrid
import net.sheltem.common.x

suspend fun main() {
    Day07().run()
}

class Day07 : Day<Long>(21, 40) {

    override suspend fun part1(input: List<String>): Long = input
        .toGrid()
        .countBeams()

    override suspend fun part2(input: List<String>): Long = input
        .toGrid()
        .countTimelines()


    private fun Grid<Char>.countBeams(): Long {
        val start = this.find('S')
        var tachyonXs = setOf<Int>(start.x)
        var splits = 0

        for (y in 1..this.maxRow) {
            val newTachyonXs = mutableSetOf<Int>()
            for (x in 0..this.maxCol) {
                if (tachyonXs.contains(x)) {
                    if (this[x to y] == '^') {
                        newTachyonXs.add(x - 1)
                        newTachyonXs.add(x + 1)
                        splits++
                    } else {
                        newTachyonXs.add(x)
                    }
                }
            }
            tachyonXs = newTachyonXs
        }

        return splits.toLong()
    }

    private fun Grid<Char>.countTimelines(): Long {
        val cache = hashMapOf<PositionInt, Long>()

        fun search(pos: PositionInt): Long = cache
            .getOrPut(pos) {
                val nextPos = pos.move(Direction.SOUTH)
                val next = this[nextPos]
                when (next) {
                    '.' -> search(nextPos)
                    '^' -> search(nextPos.move(WEST)) + search(nextPos.move(EAST))
                    else -> 1
                }
            }

        return search(this.find('S'))
    }
}
