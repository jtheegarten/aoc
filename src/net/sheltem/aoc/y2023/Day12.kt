package net.sheltem.aoc.y2023

suspend fun main() {
    Day12().run()
}

class Day12 : Day<Long>(21, 525152) {

    override suspend fun part1(input: List<String>): Long = input.map(SpringGroup::from).sumOf { it.possibleCombinations() }

    override suspend fun part2(input: List<String>): Long = input.map { SpringGroup.from(it, 4) }.sumOf { it.possibleCombinations() }

    data class SpringGroup(val line: String, val record: List<Int>) {

        private val maxLengths = IntArray(line.length) { line.drop(it).takeWhile { char -> char != '.' }.length }
        private val combinations = mutableMapOf<Pair<Int, Int>, Long>()

        fun possibleCombinations(charsUsed: Int = 0, numbersPlaced: Int = 0): Long = when {
            numbersPlaced == record.size -> if (line.drop(charsUsed).none { char -> char == '#' }) 1L else 0
            charsUsed >= line.length -> 0L
            else -> {
                if (combinations[charsUsed to numbersPlaced] == null) {
                    val take =
                        if (canTake(charsUsed, record[numbersPlaced])) possibleCombinations(charsUsed + record[numbersPlaced] + 1, numbersPlaced + 1) else 0L
                    val dontTake = if (line[charsUsed] != '#') possibleCombinations(charsUsed + 1, numbersPlaced) else 0L
                    combinations[charsUsed to numbersPlaced] = take + dontTake
                }
                combinations[charsUsed to numbersPlaced]!!
            }
        }

        private fun canTake(index: Int, toTake: Int) = maxLengths[index] >= toTake && (index + toTake == line.length || line[index + toTake] != '#')

        companion object {
            fun from(fullRecord: String, add: Int = 0) = fullRecord
                .split(" ")
                .let { (line, record) ->
                    SpringGroup(line + "?$line".repeat(add), (record + ",$record".repeat(add)).split(",").map(String::toInt))
                }
        }
    }

}

