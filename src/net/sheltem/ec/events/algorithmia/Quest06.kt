package net.sheltem.ec.events.algorithmia

import net.sheltem.common.BFS

suspend fun main() {
    Quest06().run()
}

class Quest06 : AlgorithmiaQuest<String>(listOf("RRB@", "RB@", "RB@")) {

    override suspend fun part1(input: List<String>): String = input
        .parse()
        .solve(1)

    override suspend fun part2(input: List<String>): String = input
        .parse()
        .solve(2)

    override suspend fun part3(input: List<String>): String = input
        .parse()
        .solve(2)

    private fun Map<String, List<String>>.solve(part: Int): String =
        BFS<String> { neighbourhood ->
            this[neighbourhood] ?: emptyList()
        }.fromWithPaths("RR", "@")
            .toList()
            .filter { it.contains("@") }
            .let { lists ->
                val goalPath = lists.minBy { list ->
                    lists.count { it.size == list.size }
                }
                when (part) {
                    1 -> goalPath.reversed().joinToString("")
                    2 -> goalPath.reversed().map { it[0] }.joinToString("")
                    else -> error("Part not implemented: $part")
                }
            }

    private fun List<String>.parse(): Map<String, List<String>> = this
        .associate { line ->
            val (v, neighboursString) = line.split(":").map { it.trim() }
            v to neighboursString.split(",")
        }

}
