package net.sheltem.aoc.y2015

suspend fun main() {
    Day11().run()
}

class Day11 : Day<String>("ghjaabcc", "ghjbbcdd") {
    override suspend fun part1(input: List<String>): String = generateSequence(input[0]) { it.increment() }.first { it.isValidPassword() }

    override suspend fun part2(input: List<String>): String = generateSequence(input[0]) { it.increment() }.filter { it.isValidPassword() }.take(2).last()
}

private fun String.pairs() = Regex("(\\w)\\1").findAll(this).map { it.value }.toList()

private fun String.increment(): String {
    var newWord = ""
    val indexOfSkippable = this.indexOfFirst { it == 'i' || it == 'o' || it == 'l' }
    if (indexOfSkippable != -1) {
        newWord = this.substring(0, indexOfSkippable) + this[indexOfSkippable].inc() + "a".repeat(this.length - indexOfSkippable -1)
        return newWord
    }
    for (index in indices.reversed()) {
        val newChar = this[index].increment()
        newWord = newChar + newWord
        if (newChar != 'a') {
            newWord = this.substring(0, index) + newWord
            break
        }
    }
    return newWord
}

private fun Char.increment() = if (this == 'z') 'a' else this.inc()
private fun String.isValidPassword() = when {
    this.any { it == 'i' || it == 'o' || it == 'l' } -> false
    this.pairs().size < 2 -> false
    this.windowed(3, 1).count { it.isIncSequence() } != 1 -> false
    else -> true
}

private fun String.isIncSequence() = this[0].inc() == this[1] && this[0].inc().inc() == this[2]
