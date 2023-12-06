package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.mapToLong
import net.sheltem.aoc.common.multiply
import net.sheltem.aoc.common.numericRegex

suspend fun main() {
    Day06().run()
}

class Day06 : Day<Long>(288, 71503) {

    override suspend fun part1(input: List<String>): Long = input.toRaces().map { it.waysToWin() }.multiply()

    override suspend fun part2(input: List<String>): Long = input.map { it.replace(" ","") }.toRaces().first().waysToWin()
}

private fun List<String>.toRaces() = map {
    numericRegex.findAll(it)
        .toList()
        .mapToLong()
}.let { it.first().zip(it.last()) }
    .map { Race(it.first, it.second) }

private data class Race(val time: Long, val distanceToBeat: Long) {
    fun waysToWin(): Long = (1..time)
        .count { pressTime ->
            distanceTraveled(pressTime, time) > distanceToBeat
        }
        .toLong()
}

private fun distanceTraveled(press: Long, duration: Long) = press * (duration - press)
