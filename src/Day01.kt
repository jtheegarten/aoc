fun main() {

    fun generateElvesList(input: List<String>): List<Int> {
        val elves = mutableListOf<Int>()
        val iter = input.iterator()

        var elfSum = 0
        while (iter.hasNext()) {
            val line = iter.next()
            if (line.isEmpty()) {
                elves.add(elfSum)
                elfSum = 0
            } else {
                elfSum += line.toInt()
            }
        }
        return elves
    }

    fun part1(input: List<String>): Int {
        return generateElvesList(input).max()
    }

    fun part2(input: List<String>): Int {
        return generateElvesList(input).sortedDescending().take(3).sum()
    }

    val input = readInput("Day01")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
