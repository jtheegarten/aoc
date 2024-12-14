package net.sheltem.aoc.y2024

import net.sheltem.common.*
import kotlin.math.max

suspend fun main() {
    Day14().run(true)
}

class Day14 : Day<Long>(12, 1206) {

    override suspend fun part1(input: List<String>): Long = input
        .map { it.toRobot(101, 103) }
        .map {
            it.copy(vec = ((it.vec.first + it.maxX) % it.maxX) to ((it.vec.second + it.maxY) % it.maxY))
        }.map { it.step(100) }
        .calc(101, 103)

    override suspend fun part2(input: List<String>): Long = input
        .map { it.toRobot(101, 103) }
        .map {
            it.copy(vec = ((it.vec.first + it.maxX) % it.maxX) to ((it.vec.second + it.maxY) % it.maxY))
        }
        .findPattern()
//        .also { it.second.print(101, 103) }
        .first.toLong()

    private data class Robot(var pos: PositionInt, val vec: PositionInt, val maxX: Int, val maxY: Int) {
        fun step(times: Int): Robot {
            val newX = (pos.first + vec.first * times) % maxX
            val newY = (pos.second + vec.second * times) % maxY
            pos = newX to newY
            return this
        }
    }

    private fun List<Robot>.findPattern(): Pair<Int, List<Robot>> {
        var situation = this
        var steps = 0
        while (true) {
            steps++
            situation = situation.map { it.step(1) }
            if (situation.map { it.pos }.toSet().size != situation.map { it.pos }.size) continue
            if (situation.map { it.pos }.largestPattern() > 15) break
        }
        return steps to situation
    }

    private fun List<PositionInt>.largestPattern(): Int {
        val visited = mutableSetOf<PositionInt>()
        var max = 0
        for (pos in this) {
            val queue = ArrayDeque(listOf(pos))
            val connected = mutableSetOf<PositionInt>()
            while (queue.isNotEmpty()) {
                val curr = queue.removeFirst()
                if (!visited.add(curr)) continue
                connected.add(curr)
                curr.neighbours().filter { this.contains(it) }.onEach(queue::add)
            }
            max = max(connected.size, max)
        }
        return max
    }

    private fun List<Robot>.calc(maxX: Int, maxY: Int): Long {
        val q1 = this.count { it.pos.first in 0..<(maxX / 2) && it.pos.second in 0..<(maxY / 2) }
        val q2 = this.count { it.pos.first in (maxX / 2 + 1)..<maxX && it.pos.second in 0..<(maxY / 2) }
        val q3 = this.count { it.pos.first in (maxX / 2 + 1)..maxX && it.pos.second in (maxY / 2 + 1)..<maxY }
        val q4 = this.count { it.pos.first in 0..<(maxX / 2) && it.pos.second in (maxY / 2 + 1)..<maxY }
        return q1.toLong() * q2 * q3 * q4
    }

    private fun String.toRobot(maxX: Int, maxY: Int) = this.regexNumbers().map { it.toInt() }.let { (a, b, c, d) -> Robot(a to b, c to d, maxX, maxY) }

    private fun List<Robot>.print(maxX: Int, maxY: Int): List<Robot> {
        println()
        for (y in 0 until maxY) {
            for (x in 0 until maxX) {
                val count = this.count { it.pos == x to y }
                print(if (count == 0) "." else count)
            }
            println()
        }
        return this
    }
}
