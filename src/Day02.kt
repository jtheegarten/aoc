import RPS.PAPER
import RPS.ROCK
import RPS.SCISSORS
import Result.DRAW
import Result.LOSS
import Result.WIN

fun main() {

    fun part1(input: List<String>): Int =
        input.sumOf { it.toGame().score() }

    fun part2(input: List<String>): Int =
        input.sumOf { it.toGame().scorePredicting() }

    val input = readInput("Day02")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")


}

fun Pair<String, String>.score() = second.toRPS().let { it.score + it.play(first.toRPS()).value }

fun Pair<String, String>.scorePredicting(): Int = Result.fromCode(second).let { it.played(first.toRPS()) + it.value }

fun String.toGame(): Pair<String, String> = split(" ").let { it.component1() to it.component2() }

fun String.toRPS() = RPS.fromCode(this)

enum class Result(val value: Int, val code: String) {
    WIN(6, "Z"),
    DRAW(3, "Y"),
    LOSS(0, "X");

    fun played(opponent: RPS) =
        when (this) {
            LOSS -> when (opponent) {
                ROCK -> SCISSORS.score
                PAPER -> ROCK.score
                SCISSORS -> PAPER.score
            }

            DRAW -> opponent.score
            WIN -> when (opponent) {
                ROCK -> PAPER.score
                PAPER -> SCISSORS.score
                SCISSORS -> ROCK.score
            }
        }

    companion object {
        fun fromCode(code: String) =
            enumValues<Result>().single { it.code == code }
    }
}

enum class RPS(val code: List<String>, val score: Int) {
    ROCK(listOf("A", "X"), 1),
    PAPER(listOf("B", "Y"), 2),
    SCISSORS(listOf("C", "Z"), 3);

    fun play(opponent: RPS) =
        when (this) {
            ROCK -> when (opponent) {
                ROCK -> DRAW
                PAPER -> LOSS
                SCISSORS -> WIN
            }

            PAPER -> when (opponent) {
                ROCK -> WIN
                PAPER -> DRAW
                SCISSORS -> LOSS
            }

            SCISSORS -> when (opponent) {
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
