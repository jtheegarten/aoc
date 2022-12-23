import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun String.lastAsInt(delimiter: String) = this.split(delimiter).last().toInt()

suspend inline fun <T, R> Iterable<T>.mapParallel(crossinline transform: (T) -> R): List<R> = coroutineScope {
    map { async { transform(it) } }.map { it.await() }
}

enum class MathOperation(val sign: String) {
    ADD("+"),
    MULTIPLY("*"),
    SUBSTRACT("-"),
    DIVIDE("/");

    fun opposite() =
        when (this) {
            ADD -> SUBSTRACT
            MULTIPLY -> DIVIDE
            SUBSTRACT -> ADD
            DIVIDE -> MULTIPLY
        }

    companion object {
        fun fromSign(value: String) = MathOperation.values().first { it.sign == value }
    }
}

enum class Direction8(val coords: Pair<Long, Long>) {
    NORTH(0L to -1),
    NORTH_EAST(1L to -1),
    EAST(1L to 0),
    SOUTH_EAST(1L to 1),
    SOUTH(0L to 1),
    SOUTH_WEST(-1L to 1),
    WEST(-1L to 0),
    NORTH_WEST(-1L to -1);

    companion object {
        fun from(point: Pair<Long, Long>) {
            values().firstOrNull { it.coords.first == point.first && it.coords.second == point.second }
                ?: error("No direction available for ${point.first}, ${point.second}!")
        }
    }
}

fun Pair<Long, Long>.neighbours(other: Collection<Direction8>): List<Pair<Long, Long>> = other.map { it.coords.first + first to it.coords.second + second }
fun Pair<Long, Long>.neighbour(other: Direction8): Pair<Long, Long> = this.first + other.coords.first to this.second + other.coords.second
fun Collection<Pair<Long, Long>>.bounds() = (minOf { it.first } to minOf { it.second }) to (maxOf { it.first } to maxOf { it.second })


infix operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>): Pair<Long, Long> = first + other.first to second + other.second
