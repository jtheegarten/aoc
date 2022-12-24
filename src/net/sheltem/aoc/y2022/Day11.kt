package net.sheltem.aoc.y2022

import net.sheltem.aoc.common.MathOperation
import net.sheltem.aoc.common.lastAsInt

class Day11 : Day<Long>(10605, 2713310158) {
    override fun part1(input: List<String>) = input.windowed(6, 7)
        .map { it.toMonkey() }
        .playRounds(20)
        .map { it.inspections }
        .sortedDescending()
        .take(2).let { (one, two) -> one * two }

    override fun part2(input: List<String>): Long = input.windowed(6, 7)
        .map { it.toMonkey(false) }
        .playRounds(10000)
        .map { it.inspections }
        .sortedDescending()
        .take(2).let { (one, two) -> one * two }
}

fun main() {
    Day11().run()
}

private fun List<Monkey>.playRounds(rounds: Int): List<Monkey> {
    repeat(rounds) {
        for (monkey in this) {
            monkey.takeTurn(this)
        }
    }
    return this
}

private fun List<String>.toMonkey(calm: Boolean = true): Monkey {
    val startingItems = this[1].substringAfter(":").split(",").map { it.trim() }.map { it.toLong() }.toMutableList()
    val operation =
        this[2]
            .substringAfter("= old ")
            .split(" ")
            .let { (op, second) -> Operation(MathOperation.fromSign(op), second.toLongOrDefault(-1L)) }
    val test = this.takeLast(3).toTest()

    return Monkey(startingItems, operation, test, calm)
}

private fun String.toLongOrDefault(default: Long) = toLongOrNull() ?: default

private fun List<String>.toTest(): Test = Test(
    this[0].lastAsInt(" ").toLong(),
    this[1].lastAsInt(" "),
    this[2].lastAsInt(" "),
)

private class Operation(val type: MathOperation, val value: Long) {
    fun execute(input: Long): Long {
        val secondOperand = if (value.toInt() == -1) input else value
        return when (type) {
            MathOperation.ADD -> input + secondOperand
            MathOperation.MULTIPLY -> input * secondOperand
            else -> input
        }
    }
}


private class Test(val divisor: Long, val successTarget: Int, val failureTarget: Int) {
    fun execute(inputVal: Long): Int = if ((inputVal % divisor).toInt() == 0) successTarget else failureTarget
}

private class Monkey(var items: MutableList<Long> = mutableListOf(), val op: Operation, val test: Test, val calm: Boolean) {

    var inspections = 0L

    fun takeTurn(monkeys: List<Monkey>) {
        val mod = monkeys.map { it.test.divisor }.reduce { acc, number -> acc * number }
        for (item in items) {
            inspections++
            val (value, target) = inspect(item, mod)
            monkeys[target].items.add(value)
        }
        items = mutableListOf()
    }

    private fun inspect(item: Long, mod: Long): Pair<Long, Int> = op.execute(item)
        .let { if (calm) it / 3L else it % mod}
        .let { newValue -> newValue to test.execute(newValue) }
}

