package net.sheltem.aoc.y2025

import net.sheltem.common.neighbours8
import net.sheltem.common.toGrid

suspend fun main() {
    Day04().run()
}

class Day04 : Day<Long>(13, 43) {

    override suspend fun part1(input: List<String>): Long = input
        .toGrid<Char>()
        .let { it to it.allCoordinates('@') }
        .let { (grid, papers) ->
            papers.count { paper ->
                paper.neighbours8 { grid[it] == '@' }.size < 4
            }
        }.toLong()


    override suspend fun part2(input: List<String>): Long = input
        .toGrid<Char>()
        .let { grid ->
            generateSequence {
                grid.allCoordinates('@')
                    .filter { paper ->
                        paper.neighbours8 { grid[it] == '@' }.size < 4
                    }.takeIf { it.isNotEmpty() }
                    ?.also {
                        grid.setAll(it, '.')
                    }?.count()
            }.sum().toLong()
        }
}
