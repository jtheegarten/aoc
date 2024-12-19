package net.sheltem.aoc.y2015

import net.sheltem.common.regexNumbers
import kotlin.math.min


suspend fun main() {
    Day14().run(true)
}

class Day14 : Day<Long>(1120, 0) {

    override suspend fun part1(input: List<String>): Long = input.map { it.toReindeer() }.maxOf { it.race(2503) }

    override suspend fun part2(input: List<String>): Long = input.map { it.toReindeer() }.race(2503)

    private fun String.toReindeer(): Triple<Long, Long, Long> = this.regexNumbers().let { Triple(it[0], it[1], it[2]) }

    private fun Triple<Long, Long, Long>.race(dur: Long): Long {
        val (speed, sprint, rest) = this
        val mult = dur / (sprint + rest)
        val rem = dur % (sprint + rest)
        return mult * (speed * sprint) + (speed * min(rem, sprint))
    }

    private fun List<Triple<Long, Long, Long>>.race(dur: Long): Long {
        val points = mutableMapOf<Int, Int>().withDefault { 0 }
        (1..dur).forEach { i ->
            val results = this.mapIndexed { index, reindeer ->
                index to reindeer.race(i)
            }.sortedByDescending { it.second }
            val max = results.first().second
            val winners = results.takeWhile { it.second == max }.map { it.first }

            winners.forEach { winner ->
                points[winner] = (points[winner] ?: 0) + 1
            }
        }
        return points.values.max().toLong()
    }
}
