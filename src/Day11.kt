import java.math.BigInteger

class Day11 : Day<Long>(10605, 2713310158) {
    override fun part1(input: List<String>) = input.joinToString("\n").split("\n\n")
        .map { it.toMonkey() }
        .playRounds(20)
        .map { it.inspections }
        .sortedDescending()
        .take(2).let { (one, two) -> one * two }

    override fun part2(input: List<String>): Long = input.joinToString("\n").split("\n\n")
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

private fun String.toMonkey(calm: Boolean = true): Monkey {
    val instructions = split("\n")
    val startingItems = instructions[1].substringAfter(":").split(",").map { it.trim() }.map { it.toBigInteger() }.toMutableList()
    val operation =
        instructions[2]
            .substringAfter("= old ")
            .split(" ")
            .let { (op, second) -> Operation(MathOperation.fromValue(op), BigInteger.valueOf(second.toLongOrDefault(-1L))) }
    val test = instructions.takeLast(3).toTest()

    return Monkey(startingItems, operation, test, calm)
}

private fun String.toLongOrDefault(default: Long) = toLongOrNull() ?: default

private fun String.lastAsInt(delimiter: String) = this.split(delimiter).last().toInt()

private fun List<String>.toTest(): Test = Test(
    this[0].lastAsInt(" ").toBigInteger(),
    this[1].lastAsInt(" "),
    this[2].lastAsInt(" "),
)

private class Operation(val type: MathOperation, val value: BigInteger) {
    fun execute(input: BigInteger): BigInteger {
        val secondOperand = if (value.toInt() == -1) input else value
        return when (type) {
            MathOperation.ADD -> input + secondOperand
            MathOperation.MULTIPLY -> input * secondOperand
        }
    }
}


private enum class MathOperation(val value: String) {
    ADD("+"),
    MULTIPLY("*");

    companion object {
        fun fromValue(value: String) = MathOperation.values().first { it.value == value }
    }
}

private class Test(val divisor: BigInteger, val successTarget: Int, val failureTarget: Int) {
    fun execute(inputVal: BigInteger): Int = if ((inputVal % divisor).toInt() == 0) successTarget else failureTarget
}

private class Monkey(var items: MutableList<BigInteger> = mutableListOf(), val op: Operation, val test: Test, val calm: Boolean) {

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

    private fun inspect(item: BigInteger, mod: BigInteger): Pair<BigInteger, Int> = op.execute(item)
        .let { if (calm) it / BigInteger.valueOf(3) else it % mod}
        .let { newValue -> newValue to test.execute(newValue) }
}

