package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.PositionInt
import net.sheltem.aoc.common.neighbours

suspend fun main() {
    Day21().run()
}

class Day21 : Day<Long>(2665, 394693535848011) {

    override suspend fun part1(input: List<String>): Long = input.steps(64)

    override suspend fun part2(input: List<String>): Long = input.steps2(26501365)

}

private fun List<String>.steps(repeats: Int): Long {
    var positions = setOf(mapIndexedNotNull { index, line -> if (line.contains("S")) line.indexOf("S") to index else null }.first())

    repeat(repeats) { positions = calculateNewPositions(positions) }

    return positions.count().toLong()
}

private fun List<String>.calculateNewPositions(positions: Set<Pair<Int, Int>>): Set<PositionInt> {
    return positions.flatMap { position ->
        position
            .neighbours()
            .toSet()
            .filter { this[((it.second % this.size) + this.size) % this.size][((it.first % this.size) + this.size) % this.size] != '#' }
    }.toSet()
}

private fun List<String>.steps2(repeats: Long): Long {
    var positions = setOf(mapIndexedNotNull { index, line -> if (line.contains("S")) line.indexOf("S") to index else null }.first())

    val grids = repeats / size
    val remainder = repeats % size

    val counts = mutableListOf<Int>()
    var steps = 0L

    // Because the grid is a square and the S line and column are free, we can expect a quadratic result (ax^2 + bx + c):
    // grids * repeats + remainder = steps => remainder + (remainder + gridsize) + (remainder + 2 * gridsize) + ...
    // We get the first three steps the old way...
    for (i in 0..2) {
        while (steps < ((i * size) + remainder)) {
            positions = calculateNewPositions(positions)
            steps++
        }
        counts.add(positions.size)
    }
    // ... then calculate a, b and c from them
    val c = counts[0]
    val aPlusB = counts[1] - c
    val twoA = counts[2] - c - (2 * aPlusB)
    val a = twoA / 2
    val b = aPlusB - a

    return a * (grids * grids) + b * grids + c
}

//this@steps.forEachIndexed { y, line ->
//    line.forEachIndexed { x, char ->
//        if (positions.contains(x to y)) print('O') else print(char)
//    }
//    println()
//}
