package net.sheltem.aoc.y2023

import net.sheltem.common.lcm
import net.sheltem.aoc.y2023.ModuleType.BC
import net.sheltem.aoc.y2023.ModuleType.CON
import net.sheltem.aoc.y2023.ModuleType.DUM
import net.sheltem.aoc.y2023.ModuleType.FF
import net.sheltem.aoc.y2023.Signal.HIGH
import net.sheltem.aoc.y2023.Signal.LOW

suspend fun main() {
    Day20().run(true)
}

class Day20 : Day<Long>(32000000, 1000001) {

    override suspend fun part1(input: List<String>): Long = input.map { it.toModule() }.sendPulse(1000)

    override suspend fun part2(input: List<String>): Long = input.map { it.toModule() }.part2()

}

private fun List<LogicModule>.part2(): Long {
    val logicModules = (this
            + LogicModule("output", DUM, LOW, emptyList(), mutableListOf())
            + LogicModule("rx", DUM, LOW, emptyList(), mutableListOf())
            )

    return logicModules.single { it.name == "roadcaster" }.targets.map { cycleStart ->
        val cycleModules = logicModules.prune(cycleStart)
        for (module in cycleModules) {
            module.parents.clear()
        }
        cycleModules.connect()

        var runs = 0L
        var found = false
        while (!found) {
            runs++

            val executionQueue = mutableListOf(Triple("button", "roadcaster", LOW))

            while (executionQueue.isNotEmpty()) {
                val (source, target, signal) = executionQueue.removeFirst()
                if (target == "rx" && signal == LOW) {
                    found = true
                }
                applyStep(cycleModules, target, executionQueue, signal, source)
            }
        }

        runs
    }.lcm()

}

private fun List<LogicModule>.prune(start: String): List<LogicModule> {
    val newBroadcaster = LogicModule("roadcaster", BC, LOW, listOf(start), mutableListOf())
    return generateSequence(listOf(newBroadcaster)) { currentModules ->
        val targets = currentModules.flatMap { it.targets }
        val newModules = (currentModules + this.filter { it.name in targets }).distinctBy { it.name }
        if (newModules.size > currentModules.size) newModules else null
    }.last()
}

private fun List<LogicModule>.sendPulse(repeats: Int): Long {
    val counter = Counter()
    val logicModules = (this
            + LogicModule("output", DUM, LOW, emptyList(), mutableListOf())
            + LogicModule("rx", DUM, LOW, emptyList(), mutableListOf())
            ).also { it.connect() }

    repeat(repeats) {
        val executionQueue = mutableListOf(Triple("button", "roadcaster", LOW))

        while (executionQueue.isNotEmpty()) {
            val (source, target, signal) = executionQueue.removeFirst()
            counter.add(signal)
            applyStep(logicModules, target, executionQueue, signal, source)
        }
    }

    return counter.value
}

private fun applyStep(
    logicModules: List<LogicModule>,
    target: String,
    executionQueue: MutableList<Triple<String, String, Signal>>,
    signal: Signal,
    source: String
) {
    val currentModule = logicModules.single { it.name == target }
    when (currentModule.type) {
        BC -> currentModule.targets.forEach { executionQueue.add((Triple(target, it, signal))) }
        FF -> if (signal == LOW) {
            currentModule.state = if (currentModule.state == LOW) HIGH else LOW
            currentModule.targets.forEach { newTarget ->
                executionQueue.add(Triple(target, newTarget, currentModule.state))
            }
        }

        CON -> {
            val parent = currentModule.parents.find { it.first == source }!!
            currentModule.parents.remove(parent)
            currentModule.parents.add(parent.first to signal)
            val outgoingSignal = if (currentModule.parents.any { it.second == LOW }) HIGH else LOW
            currentModule.targets.forEach { newTarget ->
                executionQueue.add(Triple(target, newTarget, outgoingSignal))
            }
        }

        DUM -> {}
    }
}

private fun String.toModule() = this.split(" -> ").let { (name, target) ->
    LogicModule(name.drop(1), ModuleType.from(name[0]), LOW, target.split(", "), mutableListOf())
}

private fun List<LogicModule>.connect() {
    forEach { module ->
        module.targets.forEach { target ->
            this.find { it.name == target }!!.parents.add(module.name to LOW)
        }
    }
}

private class Counter(var lows: Int = 0, var highs: Int = 0) {
    val value get() = lows.toLong() * highs

    fun add(signal: Signal) {
        if (signal == LOW) {
            lows += 1
        } else {
            highs += 1
        }
    }
}

private data class LogicModule(
    val name: String,
    val type: ModuleType,
    var state: Signal,
    val targets: List<String>,
    val parents: MutableList<Pair<String, Signal>>
)

private enum class ModuleType(val char: Char) {
    BC('b'),
    FF('%'),
    CON('&'),
    DUM('d');

    companion object {
        fun from(char: Char) = entries.single { it.char == char }
    }
}

private enum class Signal {
    LOW,
    HIGH;
}
