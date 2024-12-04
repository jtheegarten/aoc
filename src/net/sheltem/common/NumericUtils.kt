package net.sheltem.common

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

fun Long.wordify(): String {

    val digitMap = mapOf(1 to "one", 2 to "two", 3 to "three", 4 to "four", 5 to "five", 6 to "six", 7 to "seven", 8 to "eight", 9 to "nine")
    val tenDigitMap = mapOf(2 to "twenty", 3 to "thirty", 4 to "forty", 5 to "fifty", 6 to "sixty", 7 to "seventy", 8 to "eighty", 9 to "ninety")
    val tenMap =
        mapOf(
            10 to "ten",
            11 to "eleven",
            12 to "twelve",
            13 to "thirteen",
            14 to "fourteen",
            15 to "fifteen",
            16 to "sixteen",
            17 to "seventeen",
            18 to "eighteen",
            19 to "nineteen"
        )

    var num = this.toString()
    var result = ""

    while (num.isNotEmpty() && num.toInt() != 0) {
        result += when (num.length) {
            1 -> digitMap[num.toInt()].also { num = "" }
            2 -> tenDigitMap[num.first().digitToInt()]?.also { num = num.drop(1) } ?: tenMap[num.toInt()]?.also { num = num.drop(2) } ?: "".also {
                num = num.drop(1)
            }

            3 -> {
                var text = (digitMap[num.first().digitToInt()] + "hundred")
                num = num.drop(1)
                if (num.toInt() != 0) {
                    text += "and"
                }
                text
            }

            4 -> (digitMap[num.first().digitToInt()] + "thousand").also { num = num.drop(1) }
            else -> ""
        }
    }
    return result
}
