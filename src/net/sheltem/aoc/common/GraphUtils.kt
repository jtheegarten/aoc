package net.sheltem.aoc.common

class SearchGraph<N>(val nodes: Set<Node<N>>, edges: Set<Edge>, bidirectional: Boolean) {

    private val edges: Set<Edge> = if (bidirectional) edges + edges.map { Edge(it.to, it.from, it.weight) } else edges

    override fun toString() = "nodes: $nodes\nEdges: $edges"

    fun minBfs(startNode: Node<N>): List<Edge> {
        val routes = startingRoutes(startNode)
        while (!routes.minBy { it.weight() }.visited().containsAll(nodes)) {
            bfsStep(
                routes.minByOrNull { it.weight() } ?: listOf(),
                routes
            )
        }

        return routes.minBy { it.weight() }
    }

    fun maxBfs(startNode: Node<N>): List<Edge> {
        val routes = startingRoutes(startNode)

        while (!routes.maxBy { it.weight() }.visited().containsAll(nodes)) {
            bfsStep(
                routes.maxByOrNull { it.weight() } ?: listOf(),
                routes
            )
        }

        return routes.maxBy { it.weight() }
    }

    private fun startingRoutes(startNode: Node<N>): MutableList<List<Edge>> =
        edges
            .filter { it.from == startNode }
            .map { listOf(it) }
            .toMutableList()

    private fun bfsStep(currentRoute: List<Edge>, routes: MutableList<List<Edge>>) {
        val potentialEdges = currentRoute
            .last()
            .to
            .let { lastVisited ->
                edges.filter { it.from == lastVisited }
            }.filterNot { currentRoute.visited().contains(it.to) }
        for (potentialEdge in potentialEdges) {
            if (!currentRoute.visited().contains(potentialEdge.to)) {
                routes.add(currentRoute + potentialEdge)
            }
        }
        routes.remove(currentRoute)

    }

    data class Node<T>(val id: T)

    data class Edge(val from: Node<*>, val to: Node<*>, val weight: Long)
}

fun Collection<SearchGraph.Edge>.weight() = sumOf { it.weight }
fun Collection<SearchGraph.Edge>.visited() = flatMap { setOf(it.from, it.to) }
