package net.sheltem.aoc.y2022

import net.sheltem.aoc.common.*
import net.sheltem.aoc.y2022.Adjacencies.*
import net.sheltem.aoc.common.Direction8.*

fun main() {
    Day23().run()
}

class Day23 : Day<Long>(110, 20) {

    override fun part1(input: List<String>): Long = input.toElvesMap().executeMoves(10)

    override fun part2(input: List<String>): Long = input.toElvesMap().executeMoves()
}

private fun MutableMap<Position, Char>.executeMoves(rounds: Int? = null): Long {
    val dirQueue = ArrayDeque(listOf(N, S, W, E))

    var i = 0

    while (true) {
        i++
        val moves = mutableMapOf<Position, MutableList<Position>>()

        val elves = elves()

        for (elf in elves) {
            val adjacentPositions = elf.neighbours(listOf(*enumValues()))

            if (adjacentPositions.intersect(elves).isEmpty()) continue

            for (dir in dirQueue) {
                if (elf.neighbours(dir.directions).none { elves.contains(it) }) {
                    (elf + dir.direction8.longCoords).let {
                        moves[it] = moves.getOrDefault(it, mutableListOf()).apply { add(elf) }
                    }
                    break
                }
            }
        }

        for ((newPosition, interestedElves) in moves) {
            if (interestedElves.size == 1) {
                this[newPosition] = '#'
                this[interestedElves.single()] = '.'
            }
        }

        dirQueue.addLast(dirQueue.removeFirst())

        if (moves.isEmpty()) {
            return i.toLong()
        }

        if (i == rounds) {
            val bounds = this.filter { it.value == '#' }.keys.bounds()
            return this.countEmpty(bounds.first, bounds.second)
        }
    }
}

private fun Map<Position, Char>.elves() = filterValues { it == '#' }.keys

private fun Map<Position, Char>.printMap() = this.keys.bounds().let { bounds ->
    for (y in bounds.first.second..bounds.second.second) {
        for (x in bounds.first.first..bounds.second.first) {
            print(this.getOrDefault(x to y, '.'))
        }
        println()
    }
}

private fun Map<Position, Char>.countEmpty(min: Position, max: Position): Long {
    val dimY = max.second - min.second + 1
    val dimX = max.first - min.first + 1
    val elves = count { it.value == '#' }
    return dimY * dimX - elves
}

private fun List<String>.toElvesMap(): MutableMap<Position, Char> =
    foldIndexed(HashMap()) { y, elvesMap, row ->
        row.forEachIndexed { x, char ->
            elvesMap[x.toLong() to y.toLong()] = char
        }
        elvesMap
    }

private enum class Adjacencies(val direction8: Direction8, val directions: List<Direction8>) {
    N(NORTH, listOf(NORTH, NORTH_EAST, NORTH_WEST)),
    S(SOUTH, listOf(SOUTH, SOUTH_EAST, SOUTH_WEST)),
    W(WEST, listOf(WEST, NORTH_WEST, SOUTH_WEST)),
    E(EAST, listOf(EAST, NORTH_EAST, SOUTH_EAST));
}

