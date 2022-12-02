import java.io.File

fun main() {

    fun generateElvesList(input: String): List<Int> =
        input
            .split("\n\n")
            .map { elf ->
                elf.lines()
                    .sumOf { it.toIntOrNull() ?: 0 }
            }

    fun part1(input: String): Int {
        return generateElvesList(input).max()
    }

    fun part2(input: String): Int {
        return generateElvesList(input).sortedDescending().take(3).sum()
    }

    val input = File("src/Day01.txt").readText()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
