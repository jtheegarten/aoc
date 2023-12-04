package net.sheltem.aoc.y2023

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.math.pow

suspend fun main() {
    Day04().run()
}

class Day04 : Day<Long>(13, 30) {

    override suspend fun part1(input: List<String>): Long {
        return input.toLotteryNumbers()
            .map { outerList ->
                outerList.component2()
                    .filter { outerList.component1().contains(it) }
            }
            .sumOf { it.score() }
    }

    override suspend fun part2(input: List<String>): Long {
        val indexedWinners = input.toLotteryNumbers()
            .mapIndexed { index, outerList ->
                index to outerList.component2()
                    .filter { outerList.component1().contains(it) }
            }
        return coroutineScope {
            indexedWinners
                .map {
                    async {
                        it.unwrap(indexedWinners)
                    }
                }.awaitAll()
                .flatten()
                .count().toLong()
        }
    }
}

private fun List<String>.toLotteryNumbers(): List<List<List<Long>>> = this.map { line ->
    line.substringAfter(":")
        .split("|")
        .map { it.trim() }
        .map { it.split(" ").filter { it.isNotBlank() }.map { it.toLong() } }
        .let { listOf(it.component1(), it.component2()) }
}

private fun List<Long>.score(): Long = if (isNotEmpty()) 2.toDouble().pow(size - 1).toLong() else 0

private fun Pair<Int, List<Long>>.unwrap(indexedWinners: List<Pair<Int, List<Long>>>): List<Int> {
    return if (this.second.score() == 0L) {
        listOf(this.first)
    } else {
        listOf(this.first) + ((this.first + 1)..(this.first + this.second.count())).map { indexedWinners[it].unwrap(indexedWinners) }.flatten()
    }
}
