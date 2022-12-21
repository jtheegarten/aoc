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
