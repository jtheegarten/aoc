package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day05().run()
}

class Day05 : Day<Long>(18, 9) {

    override suspend fun part1(input: List<String>): Long = input.parse().applyRules()

    override suspend fun part2(input: List<String>): Long = 9L
}

private fun List<String>.parse(): Pair<List<Rule>, List<List<Long>>> = indexOf("").let { this.take(it).map { Rule.from(it) } to this.drop(it + 1).map { it.regexNumbers() } }

private fun Pair<List<Rule>, List<List<Long>>>.applyRules(): List<String> {
    this.second.filter { it.apply { this.first } }
}

private fun List<Long>.apply(rules: List<Rule>): Boolean = rules.all { rule -> rule.check(this) }

private class Rule(num: Long, prereq: Long) {
    fun check(list: List<Long>): Boolean

    companion object {
        fun from(input: String): Rule =
            input.regexNumbers().let { (pre, num) -> Rule(num, pre) }
    }
}
