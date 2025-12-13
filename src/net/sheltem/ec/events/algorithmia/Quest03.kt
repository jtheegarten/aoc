package net.sheltem.ec.events.algorithmia

import net.sheltem.common.Grid
import net.sheltem.common.neighbours
import net.sheltem.common.neighbours8
import net.sheltem.common.toGrid

suspend fun main() {
    Quest03().run()
}

class Quest03 : AlgorithmiaQuest<Long>(listOf(35, 35, 29)) {

    override suspend fun part1(input: List<String>): Long = input
        .toGrid { c -> if (c == '#') 0 else -1 }
        .excavate()

    override suspend fun part2(input: List<String>): Long = part1(input)

    override suspend fun part3(input: List<String>): Long = input
        .toGrid { c -> if (c == '#') 0 else -1 }
        .excavate(true)

    private fun Grid<Int>.excavate(diagonals: Boolean = false): Long {
        var excavation = this.allCoordinates(0)

        var depth = 0
        var result = 0
        while (excavation.isNotEmpty()) {
            depth++
            result += excavation.size

            excavation.forEach { pos ->
                this[pos] = depth
            }
            excavation = this.allCoordinates(depth).filter { candidate ->
                if (diagonals) {
                    candidate.neighbours8 { this[it] == depth }.size == 8
                } else {
                    candidate.neighbours { this[it] == depth }.size == 4
                }
            }
        }
        return result.toLong()
    }
}
