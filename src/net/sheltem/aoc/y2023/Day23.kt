package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.*
import net.sheltem.aoc.common.Direction.NEUTRAL

suspend fun main() {
    Day23().run()
}

class Day23 : Day<Long>(94, 154) {

    override suspend fun part1(input: List<String>): Long {
        return input.dfs(input[0].indexOf('.') to 0, input.last().indexOf('.') to (input.size - 1))
    }

    override suspend fun part2(input: List<String>) = input.adjacencies().dfs2(input[0].indexOf('.') to 0, input.last().indexOf('.') to (input.size - 1))!!.toLong()
}

private fun List<String>.adjacencies(): Map<PositionInt, Map<PositionInt, Int>> {

    val adjacencies = indices.flatMap { y ->
        this[y].indices.mapNotNull { x ->
            if (this[y][x] != '#') {
                (x to y) to Direction.cardinals.mapNotNull { dir ->
                    val next = (x to y).move(dir)
                    if (next.within(this) && this.charAt(next) != '#') next to 1 else null
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

private fun Map<PositionInt, Map<PositionInt, Int>>.dfs2(current: PositionInt, goal: PositionInt, visited: MutableMap<PositionInt, Int> = mutableMapOf()): Int? {

    if (current == goal) {
        return visited.values.sum()
    }

    var max: Int? = null

    (this[current] ?: emptyMap()).forEach { (neighbour, steps) ->
        if (neighbour !in visited) {
            visited[neighbour] = steps
            val next = this.dfs2(neighbour, goal, visited)
            max = next nullsafeMax max
            visited.remove(neighbour)
        }
    }
    return max
}

private fun List<String>.dfs(start: PositionInt, goal: PositionInt, visited: MutableSet<PositionInt> = mutableSetOf(), steps: Long = 0L): Long {
    if (start == goal) {
        return steps
    }
    visited.add(start)
    val max = (Direction.entries - NEUTRAL)
        .mapNotNull { direction ->
            val newPosition = start.move(direction)
            if (newPosition !in visited
                && newPosition.within(this)
                && this.charAt(newPosition) != '#'
                && (this.charAt(start) == '.' || Direction.from(this.charAt(start).toString()) == direction)
            ) newPosition else null
        }.maxOfOrNull { this.dfs(it, goal, visited, steps + 1) } ?: 0
    if (start in visited) visited.remove(start)
    return max
}
