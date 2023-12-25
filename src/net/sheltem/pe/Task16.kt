package net.sheltem.pe

import java.math.BigInteger

fun main() {

    println(BigInteger.valueOf(2).pow(1000).toString().sumOf { it.digitToInt() })
}
