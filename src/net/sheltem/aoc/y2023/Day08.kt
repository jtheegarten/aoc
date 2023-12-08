package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.lcm
import net.sheltem.aoc.y2023.Day08.Tree
import net.sheltem.aoc.y2023.Day08.Tree.Node

suspend fun main() {
    Day08().run()
}

class Day08 : Day<Long>(2, 6) {

    override suspend fun part1(input: List<String>): Long =
        input
            .drop(2)
            .toTree()
            .traverse("AAA", "ZZZ", input[0])

    override suspend fun part2(input: List<String>): Long =
        input
            .drop(2)
            .toTree()
            .let { tree ->
                tree.nodes
                    .keys
                    .filter { it.endsWith("A") }
                    .map {
                        tree.traverse(it, "ZZZ", input[0], true)
                    }.lcm()
            }

    class Tree(val nodes: Map<String, Node>) {

        fun traverse(start: String, goal: String, instructions: String, shortGoal: Boolean = false): Long {
            return step(start, goal, instructions, 0, 0, shortGoal)
        }

        private tailrec fun step(current: String, goal: String, instructions: String, instructionsIndex: Int, steps: Long, shortGoal: Boolean): Long = when {
            shortGoal && current.endsWith(goal[2]) -> steps
            current == goal -> steps
            else -> step(
                this.traverseNode(current, instructions[instructionsIndex] == 'L'),
                goal,
                instructions,
                (instructionsIndex + 1).mod(instructions.length), steps + 1, shortGoal
            )
        }

        private fun traverseNode(name: String, left: Boolean) = nodes[name]!!.let { if (left) it.left else it.right }

        data class Node(val name: String, val left: String, val right: String)
    }


}

private fun List<String>.toTree() = map { line ->
    Regex("\\w+").findAll(line)
        .map { it.value }
        .toList()
        .let { Node(it[0], it[1], it[2]) }
}.associateBy { it.name }
    .let(::Tree)
