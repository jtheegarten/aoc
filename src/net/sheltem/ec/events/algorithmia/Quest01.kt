package net.sheltem.ec.events.algorithmia

import java.io.File

suspend fun main() {
    Quest01().run()
}

fun generateInputFiles() {
    File("src/net/sheltem/ec/events/algorithmia/data/Quest01_test1.txt")
    File("src/net/sheltem/ec/events/algorithmia/data/Quest01_test2.txt")
    File("src/net/sheltem/ec/events/algorithmia/data/Quest01_test3.txt")
    File("src/net/sheltem/ec/events/algorithmia/data/Quest01-1.txt")
    File("src/net/sheltem/ec/events/algorithmia/data/Quest01-2.txt")
    File("src/net/sheltem/ec/events/algorithmia/data/Quest01-3.txt")
}

class Quest01 : AlgorithmiaQuest<Long>(listOf(5, 28, 30)) {

    private val costs = mapOf('A' to 0, 'B' to 1, 'C' to 3, 'D' to 5)

    override suspend fun part1(input: List<String>): Long = input
        .joinToString("")
        .sumOf { it.toString().toCost() }

    override suspend fun part2(input: List<String>): Long = input
        .joinToString("")
        .windowed(2, 2)
        .sumOf { set ->
            set.filter { it != 'x' }
                .toCost()
        }

    override suspend fun part3(input: List<String>): Long = input
        .joinToString("")
        .windowed(3, 3)
        .sumOf { set ->
            set.filter { it != 'x' }
                .toCost()
        }

    private fun String.toCost(): Long = this
        .sumOf { costs[it] ?: 0 }
        .let { sum ->
            when (this.length) {
                2 -> sum + 2
                3 -> sum + 6
                else -> sum
            }
        }.toLong()
}
