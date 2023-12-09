package net.sheltem.aoc.y2015

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper


suspend fun main() {

    Day12().run()
}

class Day12 : Day<Long>(0, 0) {
    override suspend fun part1(input: List<String>): Long = input[0].numbers().sum()

    override suspend fun part2(input: List<String>): Long = ObjectMapper().readValue(input[0], JsonNode::class.java).toNumbers().sum()
}

private fun String.numbers() = Regex("(-*\\d+)").findAll(this).map { it.value.toLong() }.toList()


private fun JsonNode.toNumbers(): List<Long> = when {
    this.isNumber -> listOf(this.asLong())
    this.hasValue("red") -> listOf(0)
    else -> this.flatMap { it.toNumbers() }
}

private fun JsonNode.hasValue(value: String): Boolean {
    if (!this.isObject) {
        return false
    }
    this.toList().forEach {
        if (value == it.asText()) return true
    }
    return false
}
