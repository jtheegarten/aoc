package net.sheltem.aoc.y2024

import net.sheltem.common.combinations
import org.jgrapht.alg.clique.BronKerboschCliqueFinder
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph

suspend fun main() {
    Day23().run()
}

class Day23 : Day<String>("7", "co,de,ka,ta") {

    override suspend fun part1(input: List<String>): String = input
        .parse()
        .let(::BronKerboschCliqueFinder)
        .iterator()
        .asSequence()
        .flatMap { clique -> if (clique.size >= 3) clique.toList().combinations(3) else emptyList() }
        .filter { clique -> clique.any{ it.startsWith('t') } }
        .toSet()
        .size
        .toString()

    override suspend fun part2(input: List<String>): String = input
        .parse()
        .let(::BronKerboschCliqueFinder)
        .maxBy { it.size }
        .sorted()
        .joinToString(",")

    private fun List<String>.parse(): SimpleGraph<String, DefaultEdge> = SimpleGraph<String, DefaultEdge>(DefaultEdge::class.java)
        .apply {
            this@parse.forEach {
                val (from, to) = it.split("-")
                addVertex(from)
                addVertex(to)
                addEdge(from, to)
            }
        }
}
