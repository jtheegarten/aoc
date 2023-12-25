package net.sheltem.aoc.y2023

import net.sheltem.common.SearchGraph
import net.sheltem.common.SearchGraph.Edge
import net.sheltem.common.SearchGraph.Node
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph


suspend fun main() {
    Day25().run()
}

class Day25 : Day<Long>(54, 5) {

    override suspend fun part1(input: List<String>) = input.solve()

    override suspend fun part2(input: List<String>) = 5L
}

private fun List<String>.solve(): Long {
    val graph = SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)
    this.forEach { line ->
        val (name, targets) = line.split(": ")
        graph.addVertex(name)
        targets.split(" ").forEach { other ->
            graph.addVertex(other)
            graph.addEdge(name, other)
        }
    }

    val oneSide = StoerWagnerMinimumCut(graph).minCut()
    return (graph.vertexSet().size - oneSide.size) * oneSide.size.toLong()
}

private fun List<String>.parseGraph(): SearchGraph<String> {
    val nodes = mutableSetOf<Node<String>>()
    val edges = mutableSetOf<Edge<String>>()

    this.forEach { line ->
        val currNode = line.take(3).let(::Node)
        val components = line.drop(5).split(" ").map(::Node)

        nodes.add(currNode)
        components.forEach { conNode ->
            nodes.add(conNode)
            val edge = Edge(currNode, conNode, 0L)
            if (edge !in edges && edge.reversed() !in edges) {
                edges.add(edge)
            }
        }
    }

    return SearchGraph(nodes, edges, true)
}
