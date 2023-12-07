package net.sheltem.aoc.y2023

import net.sheltem.aoc.common.count

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

private fun List<PokerHand>.toWinnings(): Long = this.foldIndexed(0L) { index, acc, pokerHand ->
    acc + ((index + 1) * pokerHand.bid)
}

private data class PokerHand(val hand: String, val bid: Long, val jokerRule: Boolean) : Comparable<PokerHand> {

    private val cards = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').let { if (jokerRule) it - 'J' + 'J' else it }

    val handCountMap = hand
        .groupingBy { it.toString() }
        .eachCount()

    val maxElement = handCountMap.minus("J").maxByOrNull { it.value }

    val handMap =
        if (!jokerRule || hand == "JJJJJ" || hand.count("J") == 0)
            handCountMap
        else
            handCountMap
                .minus("J")
                .minus(maxElement!!.key)
                .plus(maxElement.key.let { it to hand.count(it) + hand.count("J") })

    val type: Int
        get() = when {
            handMap.size == 1 -> 7 // five of a kind
            handMap.values.contains(4) -> 6 // four of a kind
            handMap.size == 2 -> 5 // full house
            handMap.size == 3 && handMap.values.contains(3) -> 4 // triple
            handMap.size == 3 && handMap.values.count { it == 2 } == 2 -> 3 // two pair
            handMap.values.count { it == 2 } == 1 -> 2 // pair
            handMap.values.sum() == 5 -> 1 // high card
            else -> (-1).also { println("Could not determine type of $hand | $handCountMap | $handMap") }
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


