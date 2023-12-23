package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.*

suspend fun main() {
    Day23().run()
}

class Day23 : Day<Long>(94, 154) {

    override suspend fun part1(input: List<String>) = input
        .adjacencies(true)
        .dfs(input[0].indexOf('.') to 0, input.last().indexOf('.') to (input.size - 1))!!.toLong()

    override suspend fun part2(input: List<String>) = input
        .adjacencies()
        .dfs(input[0].indexOf('.') to 0, input.last().indexOf('.') to (input.size - 1))!!.toLong()
}

private fun List<String>.adjacencies(icy: Boolean = false): Map<PositionInt, Map<PositionInt, Int>> {

    val adjacencies = indices.flatMap { y ->
        this[y].indices.mapNotNull { x ->
            if (this[y][x] != '#') {
                (x to y) to Direction.cardinals.mapNotNull { dir ->
                    val current = x to y
                    val next = current.move(dir)
                    if (next.within(this)
                        && this.charAt(next) != '#'
                        && (!icy || (this.charAt(current) == '.' || Direction.from(this.charAt(current).toString()) == dir))
                    ) {
                        next to 1
                    } else {
                        null
                    }
                }.toMap(mutableMapOf())
            } else null
        }
    }.toMap(mutableMapOf())

    adjacencies.keys.toList().forEach { key ->
        adjacencies[key]?.takeIf { it.size == 2 }?.let { neighbors ->
            val left = neighbors.keys.first()
            val right = neighbors.keys.last()
            val totalSteps = neighbors[left]!! + neighbors[right]!!
            adjacencies.getOrPut(left) { mutableMapOf() }.merge(right, totalSteps, ::maxOf)
            adjacencies.getOrPut(right) { mutableMapOf() }.merge(left, totalSteps, ::maxOf)
            listOf(left, right).forEach { adjacencies[it]?.remove(key) }
            adjacencies.remove(key)
        }
    }

    return adjacencies
}

private fun Map<PositionInt, Map<PositionInt, Int>>.dfs(current: PositionInt, goal: PositionInt, visited: MutableMap<PositionInt, Int> = mutableMapOf()): Int? {
    if (current == goal) {
        return visited.values.sum()
    }

    var max: Int? = null

    (this[current] ?: emptyMap()).forEach { (neighbour, steps) ->
        if (neighbour !in visited) {
            visited[neighbour] = steps
            val next = this.dfs(neighbour, goal, visited)
            max = next nullsafeMax max
            visited.remove(neighbour)
        }
    }
    return max
}
