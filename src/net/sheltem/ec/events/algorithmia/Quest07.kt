package net.sheltem.ec.events.algorithmia

import net.sheltem.common.mapParallel
import kotlin.math.max
import kotlin.to

suspend fun main() {
    Quest07().run(listOf(false, false, true))
}

class Quest07 : AlgorithmiaQuest<String>(listOf("BDCA", "DCBA", "")) {

    private val opponent = Chariot("A", "++-+-+=-=+=".map { it.toAction() })

    override suspend fun part1(input: List<String>): String = input
        .parse()
        .race(10)

    override suspend fun part2(input: List<String>): String = input
        .parseWithTrack()
        .raceRounds(10)
        .joinToString("") { it.first.name[0].toString() }

    override suspend fun part3(input: List<String>): String = input[0]
        .raceAll(2024)

    private suspend fun String.raceAll(rounds: Int): String {
        val opponentScore = opponent.race(10, this.map { c -> c.toAction() }, rounds)

        return combinations()
            .map { Chariot(it, it.map{ c -> c.toAction() }) }
            .mapParallel { chariot ->
                chariot to chariot.race(10, this.map { it.toAction() }, rounds)
            }.count { runResult ->
                runResult.second > opponentScore
            }
            .toString()
    }

    private fun List<Chariot>.race(repeats: Int): String = this
        .map { it.race(10, repeats) to it }
        .sortedByDescending { it.first }
        .joinToString("") { it.second.name[0].toString() }


    private fun List<String>.parse(): List<Chariot> = this
        .map { line ->
            val (name, actions) = line.split(":")
            Chariot(name, actions.filter { it != ',' }.map { it.toAction() })
        }

    private suspend fun Pair<String, List<Chariot>>.raceRounds(rounds: Int): List<Pair<Chariot, Long>> = this
        .second
        .mapParallel { chariot ->
            chariot to chariot.race(10, this.first.map { it.toAction() }, rounds)
        }.sortedByDescending { it.second }

    fun combinations(): Set<String> {
        val symbols = List(5) { '+' } + List(3) { '-' } + List(3) { '=' }
        val results = mutableSetOf<String>()

        fun CharArray.swap(i: Int, j: Int) {
            val tmp = this[i]
            this[i] = this[j]
            this[j] = tmp
        }

        fun permute(arr: CharArray, l: Int) {
            if (l == arr.size - 1) {
                results += arr.concatToString()
            } else {
                val seen = mutableSetOf<Char>()
                for (i in l until arr.size) {
                    if (arr[i] !in seen) {
                        seen += arr[i]
                        arr.swap(l, i)
                        permute(arr, l + 1)
                        arr.swap(l, i)
                    }
                }
            }
        }

        permute(symbols.toCharArray(), 0)
        return results
    }

    private fun Char.toAction(): Int = when (this) {
        '+' -> 1
        '-' -> -1
        else -> 0
    }

    private fun List<String>.parseWithTrack(): Pair<String, List<Chariot>> = this[0] to this.drop(1).parse()

    private data class Chariot(
        val name: String,
        val actions: List<Int>,
    ) {

        fun race(startVal: Int, repeats: Int): Int =
            generateSequence(0 to startVal) { (index, value) ->
                (index + 1) to actions[index % actions.size] + value
            }.take(repeats + 1)
                .drop(1)
                .sumOf { it.second }

        fun race(startVal: Long, track: List<Int>, rounds: Int): Long =
            generateSequence(0 to startVal) { (index, value) ->
                (index + 1) to step(value, track[(index + 1) % track.size], index)
            }.take((rounds * track.size) + 1)
                .drop(1)
                .sumOf { it.second }

        fun step(power: Long, trackAction: Int, index: Int): Long =
            if (trackAction == 0) {
                actions[index % actions.size] + power
            } else {
                power + trackAction
            }.coerceAtLeast(0)
    }
}

//    private fun Grid<Char>.toTrack(): String {
//        this.print()
//
//        var pos = 1 to 0
//        val visited = mutableSetOf(0 to 0)
//        val track = mutableListOf('S')
//        var neighbours = pos.neighbours { !visited.contains(it) && this.contains(it) && this[it] != '.' }
//
//        while (neighbours.isNotEmpty()) {
//            val neighbour = neighbours.single()
//            track += this[neighbour]!!
//            visited += pos
//            pos = neighbour
//
//            neighbours = pos.neighbours { !visited.contains(it) && this.contains(it) && this[it] != '.' }
//            println(neighbours)
//        }
//
//        return track.joinToString("")
//    }
