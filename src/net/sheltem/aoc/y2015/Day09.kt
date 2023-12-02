package net.sheltem.aoc.y2015

suspend fun main() {
    Day09().run()
}

class Day09 : Day<Long>(605, 72) {
    override suspend fun part1(input: List<String>): Long {
        val graph = DistanceGraph.from(input)
        return graph.nodes
            .map { graph.bfs(it, listOf(), listOf()) }.let { 5 }
    }

    override suspend fun part2(input: List<String>): Long = TODO()

    class DistanceGraph(val nodes: Set<Node>, val edges: Set<Edge>) {

        override fun toString() = "Nodes: $nodes\nEdges: $edges"

        data class Node(val name: String) {
            override fun toString() = name
        }

        data class Edge(val from: Node, val to: Node, val distance: Long) {
            override fun toString() = "$from -> $to | $distance"
        }

        fun bfs(start: Node, currentRoute: List<Edge>, routes: List<Pair<Long, List<Edge>>>): List<Pair<Long, List<Edge>>> {
            edges
                .filter { edge -> edge.from == start && !currentRoute.map { it.to }.contains(edge.to) }
                .associateBy { it.distance }
                .entries
                .sortedBy { it.key }


            return routes
        }


        companion object {
            fun from(input: List<String>): DistanceGraph {
                val nodes = mutableSetOf<Node>()
                val edges = mutableSetOf<Edge>()
                input.forEach { routeDescription ->
                    val distance = routeDescription.split(" = ").last().toLong()
                    val fromTo = routeDescription.split(" = ").first().split(" to ").take(2).map { Node(it) }
                    nodes += fromTo
                    edges += Edge(fromTo.component1(), fromTo.component2(), distance)
                }
                return DistanceGraph(nodes, edges)
            }
        }
    }
}
