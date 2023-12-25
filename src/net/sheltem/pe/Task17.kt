package net.sheltem.pe

import net.sheltem.common.wordify

fun main() {

    println((1L..1000).sumOf { it.wordify().length })
}

