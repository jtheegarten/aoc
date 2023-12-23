package net.sheltem.aoc.common

import kotlin.math.abs

infix fun Int?.nullsafeMax(other: Int?): Int? = when {
    other == null -> this
    this == null -> other
    else -> maxOf(this, other)
}
infix operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>): Pair<Long, Long> = first + other.first to second + other.second
fun Pair<Long, Long>.absoluteDifference(): Long = let { (a, b) -> if (abs(a) >= abs(b)) abs(a) - abs(b) else abs(b) - abs(a) }

fun Collection<Long>.multiply(): Long = reduce { acc, l -> acc * l }

fun List<Long>.gcd(): Long {
    var result = this[0]
    for (i in 1 until this.size) {
        var num1 = result
        var num2 = this[i]
        while (num2 != 0L) {
            val temp = num2
            num2 = num1 % num2
            num1 = temp
        }
        result = num1
    }
    return result
}

fun Pair<Long, Long>.lcm(): Long {
    val larger = if (first > second) first else second
    val maxLcm = first * second
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % first == 0L && lcm % second == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun List<Long>.lcm(): Long {
    var result = this[0]
    for (i in 1 until this.size) {
        result = (result to this[i]).lcm()
    }
    return result
}

fun <T : Comparable<T>> maxOfOrNull(a: T?, b: T?): T? {
    return when {
        a == null -> b
        b == null -> a
        else -> maxOf(a, b)
    }
}
