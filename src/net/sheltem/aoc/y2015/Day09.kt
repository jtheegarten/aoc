package net.sheltem.aoc.y2015

import net.sheltem.aoc.common.SearchGraph
import net.sheltem.aoc.common.SearchGraph.Edge
import net.sheltem.aoc.common.SearchGraph.Node
import net.sheltem.aoc.common.weight

suspend fun main() {
    Day09().run()
}

class Day09 : Day<Long>(605, 982) {
    override suspend fun part1(input: List<String>): Long = input.toSearchGraph().let { graph ->
        graph.nodes
            .map { graph.minBfs(it) }
    }.minOf { it.weight() }

    override suspend fun part2(input: List<String>): Long = input.toSearchGraph().let { graph ->
        graph.nodes
            .map { graph.maxBfs(it) }
    }.maxOf { it.weight() }


}

private fun List<String>.toSearchGraph(): SearchGraph<String> {
    val nodes = mutableSetOf<Node<String>>()
    val edges = mutableSetOf<Edge>()
    this.forEach { routeDescription ->
        val distance = routeDescription.split(" = ").last().toLong()
        val fromTo = routeDescription.split(" = ").first().split(" to ").take(2).map { Node(it) }
        nodes += fromTo
        edges += Edge(fromTo.component1(), fromTo.component2(), distance)
    }

    return SearchGraph(nodes, edges, true)
}

