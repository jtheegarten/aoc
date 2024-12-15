package net.sheltem.aoc.y2024

import net.sheltem.common.PositionInt
import net.sheltem.common.charAtOrNull
import net.sheltem.common.corners
import net.sheltem.common.neighbours

suspend fun main() {
    Day12().run()
}

class Day12 : Day<Long>(1930, 1206) {

    override suspend fun part1(input: List<String>): Long = input.solve()

    override suspend fun part2(input: List<String>): Long = input.solve(true)

    private fun List<String>.solve(second: Boolean = false): Long =
        mutableSetOf<PositionInt>()
            .let { visited ->
                this.mapIndexed { y, row ->
                    row.mapIndexed { x, c ->
                        if (visited.contains(x to y)) 0L else {
                            val plot = mutableSetOf<PositionInt>()
                            val queue = ArrayDeque(listOf(x to y))
                            var perimeter = 0
                            while (queue.isNotEmpty()) {
                                val pos = queue.removeFirst()
                                if (!visited.add(pos)) continue
                                plot.add(pos)
                                val (inside, outside) = pos.neighbours().partition { this.charAtOrNull(it) == c }
                                perimeter += if (second) this.corners(pos) else outside.size
                                queue.addAll(inside)
                            }
                            perimeter * plot.size.toLong()
                        }
                    }.sum()
                }.sum()
            }
}