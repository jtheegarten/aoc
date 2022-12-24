package net.sheltem.aoc.y2022

import net.sheltem.aoc.common.Day

fun main() {
    Day16().run()
}

class Day16 : Day<Int>(1651, 1707) {


    override fun part1(input: List<String>): Int {
        val valves = input.map(Valve.Companion::from)
        val valvesMap = valves.associateBy { it.name }
        val paths = valves.associate { it.name to it.neighbours.associateWith { 1 }.toMutableMap() }.toMutableMap()

        return paths.floydWarshall(valvesMap).dfs(valvesMap, 0, "AA", emptySet(), 0, 30)
    }

    override fun part2(input: List<String>): Int {
        val valves = input.map(Valve.Companion::from)
        val valvesMap = valves.associateBy { it.name }
        val paths = valves.associate { it.name to it.neighbours.associateWith { 1 }.toMutableMap() }.toMutableMap()

        return paths.floydWarshall(valvesMap).dfs(valvesMap, 0, "AA", emptySet(), 0, 26, true)
    }
}

private fun Map<String, MutableMap<String, Int>>.dfs(
    valvesMap: Map<String, Valve>,
    currentScore: Int,
    currentValve: String,
    visited: Set<String>,
    time: Int,
    maxTime: Int,
    partTwo: Boolean = false
): Int {
    var score = currentScore
    for ((name, dist) in this[currentValve]!!) {
        if (!visited.contains(name) && time + dist + 1 < maxTime) {

            score = score.coerceAtLeast(
                this.dfs(
                    valvesMap,
                    currentScore = currentScore + (maxTime - time - dist - 1) * valvesMap[name]?.flowRate!!,
                    currentValve = name,
                    visited = visited + name,
                    time = time + dist + 1,
                    maxTime = maxTime,
                    partTwo
                )
            )
        }
    }
    if (partTwo) {
        score = score.coerceAtLeast(
            this.dfs(
                valvesMap,
                currentScore = currentScore,
                currentValve = "AA",
                visited = visited,
                time = 0,
                maxTime = maxTime
            )
        )
    }
    return score
}

private fun MutableMap<String, MutableMap<String, Int>>.floydWarshall(valveMap: Map<String, Valve>): MutableMap<String, MutableMap<String, Int>> {

    for (outerKey in this.keys) {
        for (middleKey in this.keys) {
            for (innerKey in this.keys) {
                val middleToOuter = this[middleKey]?.get(outerKey) ?: 9999
                val outerToInner = this[outerKey]?.get(innerKey) ?: 9999
                val middleToInner = this[middleKey]?.get(innerKey) ?: 9999
                if (middleToOuter + outerToInner < middleToInner) {
                    this[middleKey]?.set(innerKey, middleToOuter + outerToInner)
                }
            }
        }
    }

    this.values.forEach {
        it.keys.map { key -> if (valveMap[key]?.flowRate == 0) key else "" }
            .forEach { toRemove -> if (toRemove != "") it.remove(toRemove) }
    }

    this.values.forEach { it.remove("AA") }
    return this

}

private data class Valve(val name: String, val flowRate: Int, val neighbours: List<String>) {
    companion object {
        fun from(line: String): Valve {
            val (name, rate) = line.split("; ")[0].split(" ").let { it[1] to it[4].split("=")[1].toInt() }
            val neighbours = line.split(", ").toMutableList()
            neighbours[0] = neighbours[0].takeLast(2)
            return Valve(name, rate, neighbours)
        }
    }
}
