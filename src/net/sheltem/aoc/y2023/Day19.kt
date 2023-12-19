package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.multiply
import net.sheltem.aoc.y2023.Day19.Part
import net.sheltem.aoc.y2023.Day19.RangePart
import net.sheltem.aoc.y2023.Day19.Rule
import net.sheltem.aoc.y2023.Day19.RuleSet

suspend fun main() {
    Day19().run()
}

class Day19 : Day<Long>(19114, 167409079868000) {

    override suspend fun part1(input: List<String>): Long = input.toRuleAndPartLists().filter().sumOf { it.value }.toLong()

    override suspend fun part2(input: List<String>): Long =
        input.toRuleAndPartLists().first.let { RangePart(mutableMapOf('x' to startRange, 'm' to startRange, 'a' to startRange, 's' to startRange)).possibilities("in", it) }

    data class Part(val values: Map<Char, Int>) {
        val value = values.values.sum()
    }

    data class RangePart(val values: Map<Char, List<Int>>) {
        val value = values.values.map { it.size.toLong() }.multiply()

        fun filterLarger(char: Char, compare: Int): RangePart = this
            .copy(values = values.minus(char).plus(char to values[char]!!.filter { it > compare }))

        fun filterSmaller(char: Char, compare: Int): RangePart = this
            .copy(values = values.minus(char).plus(char to values[char]!!.filter { it < compare }))
    }

    data class RuleSet(val name: String, val rules: List<Rule>) {
        fun apply(part: Part): String {
            rules.forEach { rule ->
                val target = rule.apply(part)
                if (target != null) {
                    return@apply target
                }
            }
            throw IllegalStateException("RuleSet $this not applicable to part $part")
        }
    }

    data class Rule(val field: Char? = null, val greaterThan: Boolean? = null, val comparator: Int? = null, val target: String) {
        fun apply(part: Part): String? = when {
            field == null -> target
            greaterThan!! -> if (part.values[field]!! > comparator!!) target else null
            else -> if (part.values[field]!! < comparator!!) target else null
        }

        fun apply(rangePart: RangePart): RangePart =
            if (greaterThan!!) rangePart.filterLarger(field!!, comparator!!)
            else rangePart.filterSmaller(field!!, comparator!!)

        fun complement(rangePart: RangePart): RangePart =
            if (!greaterThan!!) rangePart.filterLarger(field!!, comparator!! - 1)
            else rangePart.filterSmaller(field!!, comparator!! + 1)
    }
}

private fun RangePart.possibilities(ruleSetName: String, ruleSets: Map<String, RuleSet>, step: Int = 0): Long {
    var possibilities = 0L
    var workPart = this
    val rule = ruleSets[ruleSetName]!!.rules[step]
    if (rule.comparator != null) {
        val complement = rule.complement(workPart)
        workPart = rule.apply(workPart)
        possibilities += complement.possibilities(ruleSetName, ruleSets, step + 1)
    }

    return when (rule.target) {
        "A" -> possibilities + workPart.value
        "R" -> possibilities
        else -> {
            possibilities + workPart.possibilities(rule.target, ruleSets)
        }
    }
}

private fun Pair<Map<String, RuleSet>, List<Part>>.filter(): List<Part> = this.second
    .filter { it.acceptedBy(this.first) }

private fun Part.acceptedBy(ruleSets: Map<String, RuleSet>): Boolean = generateSequence("in") {
    ruleSets[it]!!.apply(this)
}.first {
    it == "R" || it == "A"
} == "A"

private fun List<String>.toRuleAndPartLists(): Pair<Map<String, RuleSet>, List<Part>> {
    val ruleSets = this.takeWhile { it != "" }.associate { line ->
        val name = line.takeWhile { it.isLetter() }
        val rules = line.drop(name.length + 1).dropLast(1).split(",").map { it.toRule() }
        name to RuleSet(name, rules)
    }

    val parts = this.takeLastWhile { it != "" }
        .map { it.drop(1).dropLast(1) }
        .map { line ->
            line.toPart()
        }

    return ruleSets to parts
}

private fun String.toRule(): Rule = when {
    this.contains(">") || this.contains("<") -> Rule(this.first(), this.contains(">"), this.split(":").first().takeLastWhile { it.isDigit() }.toInt(), this.split(":").last())
    else -> Rule(target = this)
}

private fun String.toPart(): Part = this.split(",")
    .map { it.split("=") }
    .map { it.last().toInt() }
    .let { (x, m, a, s) ->
        Part(mapOf('x' to x, 'm' to m, 'a' to a, 's' to s))
    }

private val startRange = (1..4000).toList()
