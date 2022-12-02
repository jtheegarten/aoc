import RPS.PAPER
import RPS.ROCK
import RPS.SCISSORS

fun main() {

    fun result(opponent: RPS, self: RPS) =
        when (opponent) {
            ROCK -> when (self) {
                ROCK -> draw
                PAPER -> win
                SCISSORS -> loss
            }

            PAPER -> when (self) {
                ROCK -> loss
                PAPER -> draw
                SCISSORS -> win
            }

            SCISSORS -> when (self) {
                ROCK -> win
                PAPER -> loss
                SCISSORS -> draw
            }
        }

    fun List<String>.score() =
        component2().toRPS().score + result(component1().toRPS(), component2().toRPS())

    fun List<String>.scorePredicting() =
        component2().toResult() + component2().toResult().played(component1().toRPS())

    fun part1(input: List<String>): Int =
        input.sumOf { it.split(" ").score() }

    fun part2(input: List<String>): Int =
        input.sumOf { it.split(" ").scorePredicting() }

    val input = readInput("Day02")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")


}

const val loss = 0
const val draw = 3
const val win = 6

fun String.toResult() =
    when (this) {
        "X" -> loss
        "Y" -> draw
        "Z" -> win
        else -> loss
    }

fun Int.played(opponent: RPS) =
    when(this) {
        0 -> when(opponent) {
            ROCK -> SCISSORS.score
            PAPER -> ROCK.score
            SCISSORS -> PAPER.score
        }
        3 -> opponent.score
        6 -> when(opponent) {
            ROCK -> PAPER.score
            PAPER -> SCISSORS.score
            SCISSORS -> ROCK.score
        }
        else -> 0
    }

fun String.toRPS() = RPS.fromCode(this)

enum class RPS(val code: List<String>, val score: Int) {
    ROCK(listOf("A", "X"), 1),
    PAPER(listOf("B", "Y"), 2),
    SCISSORS(listOf("C", "Z"), 3);

    companion object {
        fun fromCode(code: String) =
            enumValues<RPS>().single { it.code.contains(code) }
    }

}
