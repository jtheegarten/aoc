package net.sheltem.aoc.y2022



fun main() {
    Day13().run()
}


class Day13 : Day<Int>(13, 140) {
    private val firstDivider = "[[2]]".toSignal()

    private val secondDivider = "[[6]]".toSignal()

    override fun part1(input: List<String>): Int = input.toPairsOfSignals().map { it.compare() }.mapIndexed { index, b -> if (b) (index + 1) else 0 }.sum()
    override fun part2(input: List<String>): Int = input.filter { it.isNotBlank() }.toListOfSignals().asSequence().plus(firstDivider).plus(secondDivider).sorted()
        .mapIndexedNotNull { index, signal ->
            if (signal == firstDivider || signal == secondDivider) {
                index + 1
            } else {
                null
            }
        }.reduce { acc, i -> acc * i }
}

private fun Pair<Signal, Signal>.compare(): Boolean = first < second

private fun List<String>.toPairsOfSignals(): List<Pair<Signal, Signal>> = windowed(2, 3).map { it.toSignalPair() }

private fun List<String>.toListOfSignals(): List<Signal> = this.map { it.toSignal() }

private fun List<String>.toSignalPair(): Pair<Signal, Signal> = this[0].toSignal() to this[1].toSignal()

private fun String.toSignal(): Signal = SignalList().readInput(this.iterator().apply { next() })

private class SignalList(var content: MutableList<Signal> = mutableListOf()) : Signal {

    var previous: Char = '0'

    fun readInput(input: Iterator<Char>): SignalList {
        while (input.hasNext()) {
            if (input.readNext()) return this
        }
        return this
    }

    private fun Iterator<Char>.readNext() = next().let { char ->
        when {
            char == ']' -> return true
            char == '[' -> SignalList().readInput(this).let { content.add(it) }.also { return false }
            char.isDigit() -> {
                var newDigit = char.digitToInt()
                newDigit = if (newDigit == 0 && previous.isDigit() && previous.digitToInt() == 1) 10 else newDigit
                previous = char
                if (newDigit == 10) {
                    content.removeLast()
                    content.add(SignalValue(newDigit))
                } else content.add(SignalValue(newDigit))
                return false
            }

            else -> {
                previous = char
                return false
            }
        }
    }

    override fun toString(): String {
        return content.joinToString(", ", "[", "]")
    }

    override fun compareTo(other: Signal): Int {
        return if (other is SignalList) {
            for (index in 0 until content.size) {
                if (index >= other.content.size) return 1
                val comparison = content[index].compareTo(other.content[index])
                if (comparison != 0) return comparison
            }
            if (other.content.size > content.size) return -1 else 0
        } else {
            this.compareTo(SignalList(mutableListOf(other)))
        }
    }
}

private class SignalValue(val content: Int) : Signal {

    override fun compareTo(other: Signal): Int =
        if (other is SignalValue) {
            content - other.content
        } else {
            SignalList(mutableListOf(this)).compareTo(other)
        }

    override fun toString(): String {
        return content.toString()
    }
}

private sealed interface Signal : Comparable<Signal>
