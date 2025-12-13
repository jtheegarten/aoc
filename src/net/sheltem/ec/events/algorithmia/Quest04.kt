package net.sheltem.ec.events.algorithmia

import kotlin.math.abs

suspend fun main() {
    Quest04().run()
}

class Quest04 : AlgorithmiaQuest<Long>(listOf(10, 10, 8)) {

    override suspend fun part1(input: List<String>): Long = input
        .map { it.toLong() }
        .let { list ->
            val min = list.min()
            list.sumOf { it - min }
        }

    override suspend fun part2(input: List<String>): Long = part1(input)

    override suspend fun part3(input: List<String>): Long = input
        .map { it.toLong() }
        .let { list ->
            list.minOf { pivot ->
                list.sumOf { abs(it - pivot) }
            }
        }

}
