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
        return generateElvesList(input).sortedDescending().subList(0, 3).sum()
    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
