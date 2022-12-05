import java.io.File
import java.util.Stack

fun main() {

    fun addLine(result: MutableMap<Int, Stack<String>>, line: String) {
        var position = 1
        while (position < line.length) {
            val char = line[position].toString()
            if (char.isNotBlank()) result[(position + 3) / 4]!!.push(char)
            position += 4
        }
    }

    fun parseStacks(stacksDescription: String): Map<Int, Stack<String>> {
        val lines = stacksDescription.lines().reversed()
        val result = mutableMapOf<Int, Stack<String>>()
        for (i in 1..(lines.first().last().digitToInt())) {
            result[i] = Stack<String>()
        }
        for (i in 1 until lines.size) {
            addLine(result, lines[i])
        }

        return result
    }

    fun applyMove(move: String, stacks: Map<Int, Stack<String>>, stableOrder: Boolean = false) {
        val (amount, from, to) = move.split(" ").mapNotNull { it.toIntOrNull() }
        if (!stableOrder) {
            repeat(amount) { stacks[to]!!.push(stacks[from]!!.pop()) }
        } else {
            List(amount) {stacks[from]!!.pop()}.reversed().forEach{ stacks[to]!!.push(it) }
        }
    }

    fun solve(input: List<String>, stableOrder: Boolean = false): String {
        val (stacksDescription, instructionsDescription) = input
        val stacks = parseStacks(stacksDescription)
        instructionsDescription.lines().filter { it.isNotEmpty() }.forEach { applyMove(it, stacks, stableOrder) }

        return stacks.values.joinToString("") { it.pop() }
    }

    val input = File("src/Day05.txt").readText().split("\n\n")
//    require(solve(input) == "CMZ")
//    require(solve(input, true) == "MCD")

    println("Part 1: ${solve(input)}")
    println("Part 2: ${solve(input, true)}")
}
