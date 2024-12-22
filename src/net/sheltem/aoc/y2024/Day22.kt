package net.sheltem.aoc.y2024

import net.sheltem.common.forEachParallel
import net.sheltem.common.sumOfParallel
import java.util.concurrent.ConcurrentHashMap

suspend fun main() {
    Day22().run()
}

class Day22 : Day<Long>(37327623, 24) {

    override suspend fun part1(input: List<String>): Long =
        input.filter { it.isNotEmpty() }.map { it.toLong() }.sumOfParallel { it.secrets(2001).last() }

    override suspend fun part2(input: List<String>): Long {
        val maxBananas = ConcurrentHashMap<List<Int>, Int>()

        input.filter { it.isNotEmpty() }
            .map { it.toLong() }
            .forEachParallel { monkey ->
                val prices = monkey.secrets().map { it.toString().last().digitToInt() }.toList()
                val changeSetsSeen = HashSet<List<Int>>()

                prices.zipWithNext { left, right -> right - left }
                    .windowed(4)
                    .forEachIndexed { index, changeSet ->
                        if (changeSetsSeen.add(changeSet))
                            maxBananas.compute(changeSet) { _, value -> (value ?: 0) + prices[index + 4] }
                    }
            }
        return maxBananas.maxOf { it.value }.toLong()
    }

    private fun Long.secrets(length: Int = 2000) = generateSequence(this) { secret ->
        listOf(6, 5, 11).fold(secret) { acc, bits -> acc.xor(if (bits == 5) acc shr bits else acc shl bits) % 16777216 }
    }.take(length)
}
