package net.sheltem.aoc.y2015

typealias Route = List<Day09.DistanceGraph.Edge>

suspend fun main() {
    Day09().run()
}

class Day09 : Day<Long>(605, 982) {
    override suspend fun part1(input: List<String>): Long = DistanceGraph.from(input).let { graph ->
        graph.nodes
            .map { graph.minBfs(it) }
    }.minOf { it.distance() }

    override suspend fun part2(input: List<String>): Long = DistanceGraph.from(input).let { graph ->
        graph.nodes
            .map { graph.maxBfs(it) }
    }.maxOf { it.distance() }

    class DistanceGraph(val nodes: Set<Node>, val edges: Set<Edge>) {

        override fun toString() = "Nodes: $nodes\nEdges: $edges"

        fun minBfs(startNode: Node): Route {
            val routes = edges.filter { it.from == startNode }.map { listOf(it) }.toMutableList()

            while (!routes.minBy { it.distance() }.visited().containsAll(nodes)) {
                val shortest = routes.minByOrNull { it.distance() } ?: listOf()
                bfsStep(shortest, routes)
            }

            return routes.minBy { it.distance() }
        }

        fun maxBfs(startNode: Node): Route {
            val routes = edges.filter { it.from == startNode }.map { listOf(it) }.toMutableList()

            while (!routes.maxBy { it.distance() }.visited().containsAll(nodes)) {
                val longest = routes.maxByOrNull { it.distance() } ?: listOf()
                bfsStep(longest, routes)
            }

            return routes.maxBy { it.distance() }
        }

        private fun bfsStep(
            longest: List<Edge>,
            routes: MutableList<List<Edge>>
        ) {
            val potentialEdges = longest
                .last()
                .to
                .let { lastVisited ->
                    edges.filter { it.from == lastVisited }
                }.filterNot { longest.visited().contains(it.to) }
            for (potentialEdge in potentialEdges) {
                if (!longest.visited().contains(potentialEdge.to)) {
                    routes.add(longest + potentialEdge)
                }
            }
            routes.remove(longest)
        }

        data class Node(val name: String) {
            override fun toString() = name
        }

        data class Edge(val from: Node, val to: Node, val distance: Long) {
            override fun toString() = "$from -> $to | $distance"
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
                return DistanceGraph(nodes, edges + edges.map { Edge(it.to, it.from, it.distance) })
            }
        }
    }
}

private fun Route.distance() = sumOf { it.distance }
private fun Route.visited() = flatMap { setOf(it.from, it.to) }
