package net.sheltem.aoc.y2025


suspend fun main() {
    Day11().run()
}

class Day11 : Day<Long>(8, 2) {

    override suspend fun part1(input: List<String>): Long = input
        .associate { it.parse() }
        .let { map ->
            dfs(map, "you")
        }

    override suspend fun part2(input: List<String>): Long = input
        .associate { it.parse() }
        .let { map ->
            dfs(map, "svr", setOf("fft", "dac"))
        }

    private fun String.parse() = split(":")
        .let { (id, destinations) ->
            id to destinations.trim().split(" ")
        }

    private fun dfs(traversalMap: Map<String, List<String>>, current: String, mustVisit: Set<String> = setOf(), memory: HashMap<String, Long> = hashMapOf()): Long =
        memory.getOrPut(current + mustVisit.joinToString("")) {
            if (current == "out") {
                if (mustVisit.isEmpty()) 1 else 0
            } else {
                traversalMap[current]?.sumOf { dfs(traversalMap, it, mustVisit - current, memory) } ?: 0
            }
        }

}
