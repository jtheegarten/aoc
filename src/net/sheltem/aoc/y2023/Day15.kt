package net.sheltem.aoc.y2023

suspend fun main() {
    Day15().run()
}

class Day15 : Day<Long>(1320, 145) {

    override suspend fun part1(input: List<String>): Long = input[0].split(",").sumOf { it.hash() }.toLong()

    override suspend fun part2(input: List<String>): Long = part2(input[0])

}

private fun String.hash(): Int =
    this.fold(0) { acc, char ->
        (acc + char.code).times(17).mod(256)
    }

private fun part2(input: String): Long {
    val boxes = (0 until 256).associateWith { mutableListOf<Pair<String, Int>>() }

    input.split(",").forEach { instruction ->
        val label = if (instruction.contains("-")) instruction.dropLast(1) else instruction.takeWhile { it.isLetter() }
        val labelHash = label.hash()
        when {
            instruction.contains("-") -> boxes[labelHash]!!.removeAll { it.first == label }

            boxes[labelHash]!!.any { it.first == label } -> boxes[labelHash]!!.replaceAll { old -> if (old.first == label) label to instruction.takeLast(1).toInt() else old }

            else -> boxes[labelHash]!!.add(label to instruction.takeLast(1).toInt())
        }
    }
    return boxes.entries.foldIndexed(0) { index, acc, box ->
        acc + box.value.foldIndexed(0) { lensIndex, innerAcc, lens ->
            innerAcc + ((index + 1) * (lensIndex + 1) * lens.second)
        }
    }
}
