package net.sheltem.aoc.y2015

suspend fun main() {
    Day07().run()
}

class Day07 : Day<Int>(72, 72) {
    override suspend fun part1(input: List<String>): Int = solve(input)


    override suspend fun part2(input: List<String>): Int = solve(input, 956)


    private fun solve(input:List<String>, overrideB: Int? = null): Int {
        var workList = if (overrideB!= null) input.map { if (it.endsWith(" -> b")) "$overrideB -> b" else it } else input

        while (true) {

            val valueMap = workList.mapNotNull {line ->
                val (what, where) = line.split(" -> ")
                what.toUShortOrNull()?.let { where to it }
            }.toMap()

            if (valueMap.containsKey("a")) break

            workList = workList.map {
                var realValues = it
                for (value in valueMap) {
                    if (realValues.startsWith("${value.key} ") || realValues.contains(" ${value.key} ") || realValues.endsWith(" ${value.key}"))
                    realValues = realValues.replace(value.key, value.value.toString())
                }
                realValues
            }

            workList = workList.map {line ->
                line.split(" -> ").let { (what, where) ->
                    when {
                        line.startsWith("NOT") -> {
                            what.removePrefix("NOT ").toUShortOrNull()?.inv()?.let { "$it -> $where" }?: line
                        }
                        line.contains("OR") -> what.split(" OR ").map { it.toUShortOrNull() }.let { (one, two) ->
                            if (one != null && two != null) "${one or two} -> $where" else line
                        }
                        line.contains("AND") -> what.split(" AND ").map { it.toUShortOrNull() }.let { (one, two) ->
                            if (one != null && two != null) "${one and two} -> $where" else line
                        }
                        line.contains("LSHIFT") -> what.split(" LSHIFT ").map { it.toUShortOrNull() }.let { (one, two) ->
                            if (one != null && two != null) "${one shl two} -> $where" else line
                        }
                        line.contains("RSHIFT") -> what.split(" RSHIFT ").map { it.toUShortOrNull() }.let { (one, two) ->
                            if (one != null && two != null) "${one shr two} -> $where" else line
                        }
                        else -> line
                    }
                }

            }
            workList = workList.mapNotNull { line ->
                if (line.split(" -> ").all { it.toUShortOrNull() != null }) null else line
            }

        }
        return workList.single { it.endsWith(" a") }.split(" ")[0].toInt()
    }
}

private infix fun UShort.shl(steps: UShort) = this.toLong().shl(steps.toInt()).toUShort()
private infix fun UShort.shr(steps: UShort) = this.toLong().shr(steps.toInt()).toUShort()
