package net.sheltem.aoc.y2024

import net.sheltem.common.Grid
import net.sheltem.common.manhattan
import net.sheltem.common.toGrid

suspend fun main() {
    Day20().run(true)
}

class Day20 : Day<Long>(6, 16) {

    override suspend fun part1(input: List<String>): Long = input.toGrid().findCheats(101)

    override suspend fun part2(input: List<String>): Long = 6L

    private fun Grid<Char>.findCheats(saved: Long): Long {
        val start = this.find('S')
        val end = this.find('E')

        val path = this.dijkstra(start, end, { this.contains(it) && this[it] != '#' })

        var cheatcount = 0L
        for (i in path!!.indices) {
            val j = (i + saved).toInt()
            (j..path.lastIndex).forEach {
                if (path[i].manhattan(path[it]) == 2L) cheatcount++
            }
        }

        return cheatcount
    }
}
