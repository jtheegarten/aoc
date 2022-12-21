import MathOperation.*
import MathOperation.Companion.fromSign

fun main() {
    Day21().run()
}

class Day21 : Day<Long>(152, 19) {

    override fun part1(input: List<String>): Long = input.toMonkey().value

    override fun part2(input: List<String>): Long = 19
}


private fun List<String>.toMonkey(): MonkeyNode = this
    .map { it.split(": ") }
    .associate { it.first() to it[1] }
    .let { MathMonkey.from(it["root"]!!, it) }


private data class MathMonkey(val op: MathOperation, val left: MonkeyNode, val right: MonkeyNode) : MonkeyNode {

    override val value = op.execute(left, right)

    companion object {
        fun from(instruction: String, instructionsMap: Map<String, String>): MonkeyNode =
            if (instruction.toLongOrNull() != null) ValueMonkey(instruction.toLong())
            else instruction.split(" ")
                .let { MathMonkey(fromSign(it[1]), from(instructionsMap[it[0]]!!, instructionsMap), from(instructionsMap[it[2]]!!, instructionsMap)) }
    }
}

private data class ValueMonkey(override val value: Long) : MonkeyNode

private interface MonkeyNode {
    val value: Long

    infix operator fun plus(other: MonkeyNode) = this.value + other.value
    infix operator fun minus(other: MonkeyNode) = this.value - other.value
    infix operator fun div(other: MonkeyNode) = this.value / other.value
    infix operator fun times(other: MonkeyNode) = this.value * other.value
}

private fun MathOperation.execute(left: MonkeyNode, right: MonkeyNode) =
    when (this) {
        ADD -> left + right
        SUBSTRACT -> left - right
        MULTIPLY -> left * right
        DIVIDE -> left / right
    }
