package net.sheltem.aoc.y2023

import net.sheltem.aoc.y2023.Day19.Part
import net.sheltem.aoc.y2023.Day19.RangePart
import net.sheltem.aoc.y2023.Day19.Rule
import net.sheltem.aoc.y2023.Day19.RuleSet

suspend fun main() {
    Day19().run()
}

class Day19 : Day<Long>(19114, 167409079868000) {

    override suspend fun part1(input: List<String>): Long = input.toRuleAndPartLists().filter().sumOf { it.value }.toLong()

    override suspend fun part2(input: List<String>): Long = input.toRuleAndPartLists().first.let { RangePart(startRange, startRange, startRange, startRange).possibilities("in", it) }

    data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
        val value = x + m + a + s
    }

    data class RangePart(val x: List<Int>, val m: List<Int>, val a: List<Int>, val s: List<Int>) {
        val value = x.size.toLong() * m.size * a.size * s.size
    }

    data class RuleSet(val name: String, val rules: List<Rule>) {
        fun apply(part: Part): String {
            var target: String?
            for (rule in rules) {
                target = rule.apply(part)
                if (target != null) {
                    return target
                }
            }
            throw IllegalStateException("RuleSet $this not applicable to part $part")
        }
    }

    data class Rule(val field: Char? = null, val greaterThan: Boolean? = null, val comparator: Int? = null, val target: String) {
        fun apply(part: Part): String? = when {
            field == null -> target
            greaterThan!! -> when (field) {
                'x' -> if (part.x > comparator!!) target else null
                'm' -> if (part.m > comparator!!) target else null
                'a' -> if (part.a > comparator!!) target else null
                's' -> if (part.s > comparator!!) target else null
                else -> null
            }

            else -> when (field) {
                'x' -> if (part.x < comparator!!) target else null
                'm' -> if (part.m < comparator!!) target else null
                'a' -> if (part.a < comparator!!) target else null
                's' -> if (part.s < comparator!!) target else null
                else -> null
            }
        }

        fun apply(rangePart: RangePart): RangePart =
            when (field!!) {
                'x' -> if (greaterThan!!) RangePart(rangePart.x.filter { it > comparator!! }, rangePart.m, rangePart.a, rangePart.s)
                else RangePart(rangePart.x.filter { it < comparator!! }, rangePart.m, rangePart.a, rangePart.s)

                'm' -> if (greaterThan!!) RangePart(rangePart.x, rangePart.m.filter { it > comparator!! }, rangePart.a, rangePart.s)
                else RangePart(rangePart.x, rangePart.m.filter { it < comparator!! }, rangePart.a, rangePart.s)

                'a' -> if (greaterThan!!) RangePart(rangePart.x, rangePart.m, rangePart.a.filter { it > comparator!! }, rangePart.s)
                else RangePart(rangePart.x, rangePart.m, rangePart.a.filter { it < comparator!! }, rangePart.s)

                's' -> if (greaterThan!!) RangePart(rangePart.x, rangePart.m, rangePart.a, rangePart.s.filter { it > comparator!! })
                else RangePart(rangePart.x, rangePart.m, rangePart.a, rangePart.s.filter { it < comparator!! })

                else -> rangePart
            }

        fun complement(rangePart: RangePart): RangePart =
            when (field!!) {
                'x' -> if (!greaterThan!!) RangePart(rangePart.x.filter { it >= comparator!! }, rangePart.m, rangePart.a, rangePart.s)
                else RangePart(rangePart.x.filter { it <= comparator!! }, rangePart.m, rangePart.a, rangePart.s)

                'm' -> if (!greaterThan!!) RangePart(rangePart.x, rangePart.m.filter { it >= comparator!! }, rangePart.a, rangePart.s)
                else RangePart(rangePart.x, rangePart.m.filter { it <= comparator!! }, rangePart.a, rangePart.s)

                'a' -> if (!greaterThan!!) RangePart(rangePart.x, rangePart.m, rangePart.a.filter { it >= comparator!! }, rangePart.s)
                else RangePart(rangePart.x, rangePart.m, rangePart.a.filter { it <= comparator!! }, rangePart.s)

                's' -> if (!greaterThan!!) RangePart(rangePart.x, rangePart.m, rangePart.a, rangePart.s.filter { it >= comparator!! })
                else RangePart(rangePart.x, rangePart.m, rangePart.a, rangePart.s.filter { it <= comparator!! })

                else -> rangePart
            }
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
        Part(x, m, a, s)
    }

private val startRange = (1..4000).toList()
