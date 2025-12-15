package net.sheltem.ec.events.algorithmia

import kotlin.collections.map
import kotlin.math.max
import kotlin.to

suspend fun main() {
    Quest07().run()
}

class Quest07 : AlgorithmiaQuest<String>(listOf("BDCA", "DCBA", "")) {

    override suspend fun part1(input: List<String>): String = input
        .parse()
        .race(10)

    override suspend fun part2(input: List<String>): String = input
        .parseWithTrack()
        .raceRounds(10)

    override suspend fun part3(input: List<String>): String = ""

    private fun List<Chariot>.race(repeats: Int): String = this
        .map { it.run(10, repeats) to it }
        .sortedByDescending { it.first }
        .joinToString("") { it.second.name }

    private fun List<String>.parse(): List<Chariot> = this
        .map { line ->
            val (name, actions) = line.split(":")
            Chariot(name, actions.filter{ it != ','}.map { Chariot.Action.from(it) })
        }

    private fun Pair<String, List<Chariot>>.raceRounds(rounds: Int): String {
        val (track, chariots) = this
        var scores = chariots.associateWith { 0L }
        var powers = chariots.associateWith { 10 }

        for (i in 1..(rounds * track.length)) {
            val trackAction = Chariot.Action.from(track[i % track.length])
            powers = powers.map { (chariot, power) ->
                chariot to chariot.step(power, trackAction, i - 1)
            }.toMap()

            scores = scores.map { (chariot, score) ->
                chariot to score + powers[chariot]!!
            }.toMap()
        }

        return scores.entries.sortedByDescending { it.value }.joinToString("") { it.key.name[0].toString() }

    }

    private fun List<String>.parseWithTrack(): Pair<String, List<Chariot>> = this[0] to this.drop(1).parse()

    private data class Chariot(
        val name: String,
        val actions: List<Action>,
    ) {

        fun run(startVal: Int, repeats: Int): Int =
            generateSequence(0 to 10) { (index, value) ->
                (index + 1) to actions[index % actions.size].apply(value)
            }.take(repeats)
                .sumOf { it.second }

        fun step(power: Int, trackAction: Action, index: Int): Int =
            when(trackAction) {
                Action.PLUS -> power + 1
                Action.MINUS -> max(0, power - 1)
                else -> when(actions[index % actions.size]) {
                    Action.PLUS -> power + 1
                    Action.MINUS -> max(0, power - 1)
                    else -> power
                }
            }

        enum class Action(val value: Char) {
            PLUS('+'),
            MINUS('-'),
            HOLD('='),
            START('S');

            fun apply(num: Int) =
                when (this) {
                    PLUS -> num + 1
                    MINUS -> max(0, num - 1)
                    else -> num
                }

            companion object {
                fun from(c: Char) = entries.single { it.value == c }
            }
        }
    }
}
