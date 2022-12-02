import RPS.PAPER
import RPS.ROCK
import RPS.SCISSORS
import Result.DRAW
import Result.LOSS
import Result.WIN

fun main() {

    fun List<String>.score() =
        component2().toRPS().score + component2().toRPS().play(component1().toRPS()).value

    fun List<String>.scorePredicting() =
        component2().toResult().value + component2().toResult().played(component1().toRPS())

    fun part1(input: List<String>): Int =
        input.sumOf { it.split(" ").score() }

    fun part2(input: List<String>): Int =
        input.sumOf { it.split(" ").scorePredicting() }

    val input = readInput("Day02")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")


}

fun String.toResult() =
    when (this) {
        "X" -> LOSS
        "Y" -> DRAW
        "Z" -> WIN
        else -> LOSS
    }

fun Result.played(opponent: RPS) =
    when(this) {
        LOSS -> when(opponent) {
            ROCK -> SCISSORS.score
            PAPER -> ROCK.score
            SCISSORS -> PAPER.score
        }
        DRAW -> opponent.score
        WIN -> when(opponent) {
            ROCK -> PAPER.score
            PAPER -> SCISSORS.score
            SCISSORS -> ROCK.score
        }
    }

fun String.toRPS() = RPS.fromCode(this)

enum class Result(val value: Int) {
    WIN(6),
    DRAW(3),
    LOSS(0);

    companion object {
        fun fromResult(result: Int) =
            enumValues<Result>().single { it.value == result }
    }
}

enum class RPS(val code: List<String>, val score: Int) {
    ROCK(listOf("A", "X"), 1),
    PAPER(listOf("B", "Y"), 2),
    SCISSORS(listOf("C", "Z"), 3);

    fun play(opponent: RPS) =
        when(this) {
            ROCK -> when(opponent) {
                ROCK -> DRAW
                PAPER -> LOSS
                SCISSORS -> WIN
            }
            PAPER -> when(opponent) {
                ROCK -> WIN
                PAPER -> DRAW
                SCISSORS -> LOSS
            }
            SCISSORS -> when(opponent) {
                ROCK -> LOSS
                PAPER -> WIN
                SCISSORS -> DRAW
            }
        }

    companion object {
        fun fromCode(code: String) =
            enumValues<RPS>().single { it.code.contains(code) }
    }

}
