package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day21().run()
}

class Day21 : Day<Long>(126384, 154115708116294) {

    private val numPad = listOf("789", "456", "123", ".0A").toGrid<Char>()
    private val dirPad = listOf(".^A", "<v>").toGrid<Char>()

    private fun listSteps(grid: Grid<Char>, start: Char, end: Char, visitedButtons: String): List<String> =
        if (start == end) listOf("A")
        else grid.find(start).neighbours { grid.contains(it) && grid[it] != '.' }
            .filterNot { visitedButtons.contains(grid[it]!!) }
            .flatMap { next ->
                listSteps(grid, grid[next]!!, end, visitedButtons + start).map { Direction.from(grid.find(start), next).caret + it }
            }

    private val dirSteps = "^<v>A".flatMap { start ->
        "^<v>A".map { end ->
            (start to end) to listSteps(dirPad, start, end, "")
        }
    }.toMap()

    private val numSteps = "0123456789A".flatMap { start ->
        "0123456789A".map { end ->
            (start to end) to listSteps(numPad, start, end, "")
        }
    }.toMap()

    private val cache = mutableMapOf<State, Long>()

    override suspend fun part1(input: List<String>): Long = input.also { cache.clear() }.filter { it.isNotEmpty() }.sumOf { it.score(3) }

    override suspend fun part2(input: List<String>): Long = input.also { cache.clear() }.filter { it.isNotEmpty() }.sumOf { it.score(26) }

    private fun String.score(depth: Int): Long = this
        .fold('A' to 0L) { (prevChar, value), c ->
            c to (value + numSteps.numPresses(State(prevChar, c, depth)))
        }.second * this.take(3).toLong()

    private fun Map<Pair<Char, Char>, List<String>>.numPresses(state: State): Long =
        cache.getOrPut(state) {
            if (state.remLevels == 0) return 1

            this[state.prevChar to state.curChar]!!.minOf { steps ->
                steps.fold(0L to 'A') { (value, prevChr), c ->
                    value + dirSteps.numPresses(State(prevChr, c, state.remLevels - 1)) to c
                }.first
            }
        }

    data class State(val prevChar: Char, val curChar: Char, val remLevels: Int)
}
