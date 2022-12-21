import MathOperation.*
import MathOperation.Companion.fromSign

fun main() {
    Day21().run()
}

class Day21 : Day<Long>(152, 301) {

    override fun part1(input: List<String>): Long = input.toMonkey().value.toLong()

    override fun part2(input: List<String>): Long = input.humanNumber().toLong()
}


private fun List<String>.toMonkey(): MonkeyNode = this
    .map { it.split(": ") }
    .associate { it.first() to it[1] }
    .let { MonkeyNode.from(it["root"]!!, it, "root") }

private fun List<String>.humanNumber(): Double {
    val inputMap = this.map { it.split(": ") }.associate { it.first() to it[1] }.toMutableMap()
    inputMap["humn"] = "0"
    val rootMonkey = MonkeyNode.from(inputMap["root"]!!, inputMap, "root") as MathMonkey
    return if (rootMonkey.left.contains("humn")) {
        (rootMonkey.left as MathMonkey).findValue("humn", rootMonkey.right.value)
    } else {
        (rootMonkey.right as MathMonkey).findValue("humn", rootMonkey.left.value)
    }
}

private data class MathMonkey(val op: MathOperation, val left: MonkeyNode, val right: MonkeyNode, override val name: String) : MonkeyNode {

    override val value = op.execute(left, right)

    fun findValue(targetNode: String, targetValue: Double): Double {

        val targetMonkey = ValueMonkey(targetValue, targetNode)

        if (left.name == targetNode) return op.opposite().execute(targetMonkey, right)
        if (right.name == targetNode) return op.opposite().execute(targetMonkey, left)

        if (left is ValueMonkey) return (right as MathMonkey).findValue(targetNode, op.opposite().execute(targetMonkey, left))
        if (right is ValueMonkey) return (left as MathMonkey).findValue(targetNode, op.opposite().execute(targetMonkey, right))

        if (left.contains(targetNode)) return (left as MathMonkey).findValue(targetNode, op.opposite().execute(targetMonkey, right))
        if (right.contains(targetNode)) return (right as MathMonkey).findValue(targetNode, op.opposite().execute(targetMonkey, left))

        throw IllegalStateException("Impossible Solution")
    }

    override fun contains(targetNode: String): Boolean =
        if (left.name == targetNode || right.name == targetNode) true
        else left.contains(targetNode) || right.contains(targetNode)

}

private data class ValueMonkey(override val value: Double, override val name: String) : MonkeyNode {
    override fun contains(targetNode: String) = false

}

private interface MonkeyNode {
    val value: Double
    val name: String

    infix operator fun plus(other: MonkeyNode) = value + other.value
    infix operator fun minus(other: MonkeyNode) = value - other.value
    infix operator fun div(other: MonkeyNode) = value / other.value
    infix operator fun times(other: MonkeyNode) = value * other.value
    fun contains(targetNode: String): Boolean

    companion object {
        fun from(instruction: String, instructionsMap: Map<String, String>, name: String): MonkeyNode =
            if (instruction.toLongOrNull() != null) ValueMonkey(instruction.toDouble(), name)
            else instruction.split(" ")
                .let {
                    MathMonkey(
                        fromSign(it[1]),
                        from(instructionsMap[it[0]]!!, instructionsMap, it[0]),
                        from(instructionsMap[it[2]]!!, instructionsMap, it[2]),
                        name
                    )
                }
    }
}

private fun MathOperation.execute(left: MonkeyNode, right: MonkeyNode) =
    when (this) {
        ADD -> left + right
        SUBSTRACT -> left - right
        MULTIPLY -> left * right
        DIVIDE -> left / right
    }
