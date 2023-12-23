package net.sheltem.aoc.y2015

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import net.sheltem.common.SearchGraph
import net.sheltem.common.SearchGraph.Edge
import net.sheltem.common.SearchGraph.Node
import net.sheltem.common.regexNumbers
import net.sheltem.common.weight


suspend fun main() {

    Day13().run(true)
}

class Day13 : Day<Long>(330, 0) {
    override suspend fun part1(input: List<String>): Long {
        val graph = input.toSearchGraph()

        graph.floydWarshall()

        return coroutineScope {
            graph
                .nodes
                .map { node ->
                    async {
                        graph.maxBfs(node)
                    }
                }.awaitAll()
                .maxOf { it.weight() }
        }
    }

    override suspend fun part2(input: List<String>): Long {
        val graph = input.plus(part2String.lines()).toSearchGraph()

        println(graph)

        return coroutineScope {
            graph
                .nodes
                .map { node ->
                    async {
                        graph.maxBfs(node)
                    }
                }.awaitAll()
                .maxOf { it.weight() }
        }
    }
}

private fun List<String>.toSearchGraph(): SearchGraph<String> {
    val nodes = this.map {
        it.split(" ").first()
    }.map { Node(it) }.toSet()

    val edges = this.map { line ->
        val positive = line.contains("gain")
        val fromTo = line.dropLast(1).split(" ").let { it.first() to it.last() }
        Edge(Node(fromTo.first), Node(fromTo.second), line.regexNumbers().single().let { if (positive) it else it * -1L })
    }.toSet()

    val combinedEdges = edges.map { edge ->
        val oppositeDirectionEdge = edges.single { it.from == edge.to && it.to == edge.from }
        Edge(edge.from, edge.to, edge.weight + oppositeDirectionEdge.weight)
    }.toSet()

    return SearchGraph<String>(nodes, combinedEdges, bidirectional = false, circular = true)
}

const val part2String = """Jon would lose 0 happiness units by sitting next to Alice.
Jon would lose 0 happiness units by sitting next to Bob.
Jon would lose 0 happiness units by sitting next to Carol.
Jon would lose 0 happiness units by sitting next to David.
Jon would lose 0 happiness units by sitting next to Eric.
Jon would lose 0 happiness units by sitting next to Frank.
Jon would lose 0 happiness units by sitting next to George.
Jon would lose 0 happiness units by sitting next to Mallory.
Alice gain 0 Jon.
Bob gain 0 Jon.
Carol gain 0 Jon.
David gain 0 Jon.
Eric gain 0 Jon.
Frank gain 0 Jon.
George gain 0 Jon.
Mallory gain 0 Jon."""
