package net.sheltem.aoc.y2024

import net.sheltem.common.*

suspend fun main() {
    Day05().run()
}

class Day05 : Day<Long>(143, 123) {

    override suspend fun part1(input: List<String>): Long = input
        .parse()
        .let { (rules, pages) ->
            pages
                .filter { page -> page.isValid(rules) }
                .sumOf { it.middle() }
        }

    override suspend fun part2(input: List<String>): Long = input
        .parse()
        .let { (rules, pages) ->
            pages.filter { page -> !page.isValid(rules) }
                .map { it.sortPages(rules) }
                .sumOf { it.middle() }
        }

    private fun List<String>.parse(): Pair<List<Pair<Long, Long>>, List<List<Long>>> = indexOf("").let { splitIndex ->
        this.take(splitIndex).map { rule ->
            rule.regexNumbers().let { ruleNumbers -> ruleNumbers[0] to ruleNumbers[1] }
        } to this.drop(splitIndex + 1).map { it.regexNumbers() }
    }

    private fun List<Long>.isValid(rules: List<Pair<Long, Long>>): Boolean = this.sortPages(rules) == this

    private fun List<Long>.sortPages(rules: List<Pair<Long, Long>>) = map { Page(it, rules) }.sorted().map { it.value }

    private fun List<Long>.middle(): Long = this[size / 2]

    private class Page(val value: Long, val rules: List<Pair<Long, Long>>) : Comparable<Page> {

        override fun compareTo(other: Page): Int = when {
            value == other.value -> 0
            value to other.value in rules -> -1
            else -> 1
        }
    }
}
