package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day20().run(true)
}

class Day20 : Day<Long>(6, 16) {

    override suspend fun part1(input: List<String>): Long = input.toGrid<Char>().findCheats(100, 2)

    override suspend fun part2(input: List<String>): Long = input.toGrid<Char>().findCheats(100, 20)

    private suspend fun Grid<Char>.findCheats(saved: Int, steps: Int): Long {
        val start = this.find('S')
        val end = this.find('E')

        val path = this.dijkstra(start, end, { this.contains(it) && this[it] != '#' })

        return path!!.indices.sumOfParallel { i ->
            val j = i + saved + 1
            (j..path.lastIndex).count {
                val distance = path[i].manhattan(path[it])
                distance <= steps.toLong() && it - i - distance >= saved
            }.toLong()
        }
    }
}
