package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day12().run()
}

class Day12 : Day<Long>(1930, 1206) {

    override suspend fun part1(input: List<String>): Long = input.toGrid<Char>().solve()

    override suspend fun part2(input: List<String>): Long = input.toGrid<Char>().solve(true)

    private fun Grid<Char>.solve(second: Boolean = false): Long =
        mutableSetOf<PositionInt>()
            .let { visited ->
                allCoordinates.sumOf { coord ->
                        if (visited.contains(coord)) 0L else {
                            val plot = mutableSetOf<PositionInt>()
                            val queue = ArrayDeque(listOf(coord))
                            var perimeter = 0
                            while (queue.isNotEmpty()) {
                                val pos = queue.removeFirst()
                                if (!visited.add(pos)) continue
                                plot.add(pos)
                                val (inside, outside) = pos.neighbours().partition { this[it] == this[coord] }
                                perimeter += if (second) this.corners(pos) else outside.size
                                queue.addAll(inside)
                            }
                            perimeter * plot.size.toLong()
                        }
                    }
                }
            }

