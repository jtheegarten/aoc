package net.sheltem.aoc.y2023

import kotlin.math.pow

suspend fun main() {
    Day04().run()
}

class Day04 : Day<Long>(13, 30) {

    override suspend fun part1(input: List<String>): Long = input.indexWinners()
        .sumOf { it.second.score() }

    override suspend fun part2(input: List<String>): Long = input.indexWinners()
        .map { it.first to it.second.count() }
        .countCards()
}

private fun List<String>.indexWinners(): List<Pair<Int, List<Long>>> = toLotteryNumbers()
    .mapIndexed { index, outerList ->
        index to outerList.component2()
            .filter { outerList.component1().contains(it) }
    }

private fun List<String>.toLotteryNumbers(): List<List<List<Long>>> = this.map { line ->
    line.substringAfter(":")
        .split("|")
        .map { it.trim() }
        .map { it.split(" ").filter { it.isNotBlank() }.map { it.toLong() } }
        .let { listOf(it.component1(), it.component2()) }
}


private fun List<Long>.score(): Long = if (isNotEmpty()) 2.toDouble().pow(size - 1).toLong() else 0

private fun List<Pair<Int, Int>>.countCards(): Long {
    val cardCounts = this.associate { it.first to 1 }.toMutableMap()
    for (index in 0..this.last().first) {
        if (this[index].second > 0) cardCounts.addCards(index, this[index].second, cardCounts[index]!!)
    }

    return cardCounts.values.sum().toLong()
}

private fun MutableMap<Int, Int>.addCards(index: Int, rangeLength: Int, amountToAdd: Int): MutableMap<Int, Int> {
    for (i in index + 1..(index + rangeLength)) {
        this[i] = this[i]!! + amountToAdd
    }
    return this
}
