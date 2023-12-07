package net.sheltem.aoc.y2023

suspend fun main() {
    Day07().run()
}

class Day07 : Day<Long>(6440, 5905) {

    override suspend fun part1(input: List<String>): Long = input.map { line ->
        line.toPokerHand()
    }.sorted()
        .toWinnings()

    override suspend fun part2(input: List<String>): Long = input.map { line ->
        line.toPokerHand(true)
    }.sorted()
        .toWinnings()
}

private fun String.toPokerHand(jokerRule: Boolean = false) = this.split(" ").let { PokerHand(it.first(), it.last().toLong(), jokerRule) }

private fun List<PokerHand>.toWinnings(): Long = this.foldIndexed(0) { index, acc, pokerHand ->
    acc + ((index + 1) * pokerHand.bid)
}

private data class PokerHand(val hand: String, val bid: Long, val jokerRule: Boolean) : Comparable<PokerHand> {

    private val cards = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').let { if (jokerRule) it - 'J' + 'J' else it }

    val handMap = hand.groupingBy { it.toString() }.eachCount().let { map ->
        if (jokerRule && map.keys.contains("J") && map.size != 1) {
            val jokerlessMap = map.minus("J")
            val maxChar = jokerlessMap.entries.maxBy { it.value }.key
            jokerlessMap.minus(maxChar).plus(maxChar to (map[maxChar]!! + map["J"]!!))
        } else {
            map
        }//.also { println("$hand -> $it") }
    }

    val type: Int
        get() = when {
            handMap.size == 1 -> 7 // five of a kind
            handMap.values.contains(4) -> 6 // four of a kind
            handMap.size == 2 -> 5 // full house
            handMap.size == 3 && handMap.values.contains(3) -> 4 // triple
            handMap.size == 3 && handMap.values.count { it == 2 } == 2 -> 3 // two pair
            handMap.values.count { it == 2 } == 1 -> 2 // pair
            handMap.values.sum() == 5 -> 1 // high card
            else -> (-1).also { println("Could not determine type of $hand") }
        }

    override fun compareTo(other: PokerHand): Int = (this.type - other.type).let { typeDifference ->
        if (typeDifference == 0) {
            this.hand.zip(other.hand).map {
                cards.indexOf(it.second) - cards.indexOf(it.first)
            }.first { it != 0 }
        } else {
            typeDifference
        }
    }
}
