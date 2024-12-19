package net.sheltem.aoc.y2015

suspend fun main() {
    Day16().run(true)
}

class Day16 : Day<Long>(6, 5) {

    val targetSue = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1
    )

    override suspend fun part1(input: List<String>): Long = input
        .map { it.toAunt() }
        .mapIndexed { index, sue -> index to sue.canBe(targetSue) }
        .single { it.second }.first.toLong() + 1

    override suspend fun part2(input: List<String>): Long = input
        .map { it.toAunt() }
        .mapIndexed { index, sue -> index to sue.canBe2(targetSue) }
        .single { it.second }.first.toLong() + 1

    private fun Map<String, Int>.canBe(target: Map<String, Int>): Boolean {
        target.entries.forEach { (k, v) ->
            this[k]?.let { if (it != v) return false }
        }
        return true
    }

    private fun Map<String, Int>.canBe2(target: Map<String, Int>): Boolean {
        target.entries.forEach { (k, v) ->
            this[k]?.let {
                when (k) {
                    "cats" -> if (it <= v) return false
                    "trees" -> if (it <= v) return false
                    "pomeranians" -> if (it >= v) return false
                    "goldfish" -> if (it >= v) return false
                    else -> if (it != v) return false
                }
            }
        }
        return true
    }

    private fun String.toAunt(): Map<String, Int> =
        this.replace(" ", "")
            .let { it.substring(it.indexOf(':') + 1) }
            .let { it.split(",") }
            .associate { att ->
                att.split(":").let { it[0] to it[1].toInt() }
            }
}

/*children: 3
cats: 7
samoyeds: 2
pomeranians: 3
akitas: 0
vizslas: 0
goldfish: 5
trees: 3
cars: 2
perfumes: 1
*/
