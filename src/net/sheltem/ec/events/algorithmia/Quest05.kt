package net.sheltem.ec.events.algorithmia

import net.sheltem.common.joinToLong
import kotlin.times

suspend fun main() {
    Quest05().run()
}

class Quest05 : AlgorithmiaQuest<Long>(listOf(2323, 50877075, 23)) {

    override suspend fun part1(input: List<String>): Long = input
        .parse()
        .dance(0, 10)

    override suspend fun part2(input: List<String>): Long = input
        .parse()
        .danceUntil(0, 2024)

    override suspend fun part3(input: List<String>): Long = input.let { 23 }

    private fun List<List<Int>>.danceUntil(
        startDIndex: Int,
        count: Int,
        startRound: Int = 1,
        memory: MutableMap<Long, Int> = mutableMapOf()
    ): Long {
        var lists = this
        var dIndex = startDIndex
        var round = startRound

        while (true) {
            val cIndex = (dIndex + 1) % lists.size
            val dancer = lists[dIndex][0]

            lists = lists.mapIndexed { index, list ->
                when (index) {
                    dIndex -> list.drop(1)
                    cIndex -> list.clap(dancer)
                    else -> list
                }
            }

            val number = lists.map { it[0] }.joinToLong()
            memory.merge(number, 1) { old, new -> old + new }
            if ((memory[number] ?: 0) == count) {
                return number * round
            }

            dIndex = (dIndex + 1) % lists.size
            round++
        }
    }

    private fun List<List<Int>>.danceUntilRec(dIndex: Int, count: Int, round: Int = 0, memory: MutableMap<Long, Int> = mutableMapOf()): Long {
        val number = this.map { it[0] }.joinToLong()
        if ((memory[number] ?: 0) == count) {
            return number * round
        }
        val cIndex = (dIndex + 1) % this.size
        val dancer = this[dIndex][0]

        return this.mapIndexed { index, list ->
            when (index) {
                dIndex -> list.drop(1)
                cIndex -> list.clap(dancer)
                else -> list
            }
        }.also {
            if (round != 0) memory.merge(number, 1) { old, new -> old + new }
        }.danceUntil((dIndex + 1) % this.size, count, round + 1, memory)
    }

    private fun List<List<Int>>.dance(dIndex: Int, repeats: Int): Long {
        if (repeats == 0) {
            return this.map { it[0] }.joinToLong()
        }

        val cIndex = (dIndex + 1) % this.size
        val dancer = this[dIndex][0]

        return this.mapIndexed { index, list ->
            when (index) {
                dIndex -> list.drop(1)
                cIndex -> list.clap(dancer)
                else -> list
            }
        }.dance((dIndex + 1) % this.size, repeats - 1)
    }

    private fun List<Int>.clap(dancer: Int): List<Int> =
        when {
            dancer == 0 -> error("WTF")
            dancer % (this.size * 2) == 0 -> this.take(1) + dancer + this.drop(1)
            dancer % (this.size * 2) <= this.size -> this.take((dancer % (this.size * 2)) - 1) + dancer + this.drop((dancer % (this.size * 2)) - 1)
            dancer % (this.size * 2) > this.size -> {
                val offset = dancer % (this.size * 2) - this.size
                val index = this.size - offset
                this.take(index + 1) + dancer + this.drop(index + 1)
            }
            else -> error("WTF")
        }

    private fun List<String>.parse() = map { it.split(" ") }
        .let { lists ->
            (0..<lists[0].size).map { position ->
                lists.map { it[position].toInt() }
            }
        }
}
