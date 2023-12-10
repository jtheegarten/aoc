package net.sheltem.aoc.common

import net.sheltem.aoc.common.SearchGraph.Edge
import net.sheltem.aoc.common.SearchGraph.Route

class SearchGraph<N>(val nodes: Set<Node<N>>, edges: Set<Edge<N>>, bidirectional: Boolean, val circular: Boolean = false) {

    val edges: Set<Edge<N>> = if (bidirectional) edges + edges.map { Edge(it.to, it.from, it.weight) } else edges

    override fun toString() = "nodes: $nodes\nEdges: $edges"

    fun bfs(startNode: Node<N>): List<List<Edge<N>>> {
        val routes = startingRoutes(startNode)

        while (!routes.all { it.visited(circular).containsAll(nodes) }) {
            bfsStep(
                routes.minBy { it.weight() },
                routes,
                startNode
            )
        }
        return routes
    }

    fun floydWarshall(negativeEdges: Boolean = false) {
        val nodeList = nodes.toList()
        val distanceMatrix = Array(nodeList.size) { LongArray(nodeList.size) { 0L } }

        for (k in nodeList.indices) {
            for (i in nodeList.indices) {
                distanceMatrix[k][i] = edges.singleOrNull { it.from == nodeList[k] && it.to == nodeList[i] }?.weight ?: Long.MAX_VALUE
            }
        }


        for (k in distanceMatrix.indices) {
            for (i in distanceMatrix.indices) {
                for (j in distanceMatrix.indices) {
                    if (distanceMatrix[i][j] > distanceMatrix[i][k] + distanceMatrix[k][j]) {
                        distanceMatrix[i][j] = distanceMatrix[i][k] + distanceMatrix[k][j]
                    }
                }
            }
        }

        printMatrix(distanceMatrix)
    }

    fun printMatrix(matrix: Array<LongArray>) {
        for (i in nodes.indices) {
            for (j in nodes.indices) {
                if (matrix[i][j] == Long.MAX_VALUE) print("INF ")
                else print("${matrix[i][j]}  ")
            }
            println()
        }
    }

    fun bfs(startNode: Node<N>, goalNode: Node<N>): Route<N> {
        val queue = ArrayDeque<Node<N>>()
        val visited = mutableListOf(startNode)
        queue.add(startNode)
        while (queue.isNotEmpty()) {
            val currentNode = queue.first()
            if (currentNode == goalNode) {
                break
            }
            edges.filter { it.from == currentNode }
                .forEach { edge ->
                    if (visited.contains(edge.to)) {
                        visited.add(edge.to)
                        queue.add(edge.to)
                    }
                }
        }

        return visited.zipWithNext()
            .map { fromTo -> edges.single { it.from == fromTo.first && it.to == fromTo.second } }
            .let(::routeOf)

    }

    fun dijkstra(startNode: Node<N>, goalNode: Node<N>): Route<N> {
        val queue = nodes.toMutableSet()
        val distances = queue.associateWith { Long.MAX_VALUE }.toMutableMap()
        val previous = queue.associateWith { null }.toMutableMap<Node<N>, Node<N>?>()

        distances[startNode] = 0

        while (queue.isNotEmpty()) {
            val currentNode = queue.minByOrNull { distances[it] ?: 0 }
            queue.remove(currentNode)

            if (currentNode == goalNode) {
                break
            }
            edges
                .filter { it.from == currentNode }
                .forEach { edge ->
                    val toNode = edge.to
                    val routeDistance = (distances[currentNode] ?: 0) + edge.weight
                    if (routeDistance < (distances[toNode] ?: 0)) {
                        distances[toNode] = routeDistance
                        previous[toNode] = currentNode
                    }
                }
        }

        return generateSequence(goalNode) { previous[it] }
            .takeWhile { previous[it] != null }
            .toList()
            .reversed()
            .zipWithNext()
            .map { fromTo -> edges.single { it.from == fromTo.first && it.to == fromTo.second } }.let(::routeOf)
    }

    fun maxBfs(startNode: Node<N>): List<Edge<N>> = bfs(startNode).maxBy { it.weight() }
    fun minBfs(startNode: Node<N>): List<Edge<N>> = bfs(startNode).minBy { it.weight() }

    private fun startingRoutes(startNode: Node<N>): MutableList<List<Edge<N>>> =
        edges
            .filter { it.from == startNode }
            .map { listOf(it) }
            .toMutableList()

    private fun bfsStep(currentRoute: List<Edge<N>>, routes: MutableList<List<Edge<N>>>, startNode: Node<N>) {
        val potentialEdges = currentRoute
            .last()
            .to
            .let { lastVisited ->
                edges.filter { it.from == lastVisited }
            }.filterNot { currentRoute.visited(circular).contains(it.to) }
            .filterNot { circular && currentRoute.visited(circular).contains(startNode) }

        for (potentialEdge in potentialEdges) {
            if (!currentRoute.visited(circular).contains(potentialEdge.to)) {
                routes.add(currentRoute + potentialEdge)
            }
        }
        routes.remove(currentRoute)

    }

    data class Node<N>(val id: N) {
        override fun toString() = "$id"
    }

    data class Edge<N>(val from: Node<N>, val to: Node<N>, val weight: Long) {
        override fun toString() = "$from -($weight)-> $to"
    }

    data class Route<N>(val edges: List<Edge<N>> = listOf()) : List<Edge<N>> by edges, Comparable<Route<N>> {
        override fun compareTo(other: Route<N>) = edges.weight() compareTo other.edges.weight()
    }
}

fun Collection<Edge<*>>.weight() = sumOf { it.weight }
fun Collection<Edge<*>>.visited(circular: Boolean = false) = flatMap { if (circular) setOf(it.to) else setOf(it.from, it.to) }
fun <N> routeOf(list: Collection<Edge<N>>): Route<N> = Route(list.toList())
fun <N> routeOf(vararg elements: Edge<N>): Route<N> = if (elements.isNotEmpty()) Route(elements.asList()) else Route()
