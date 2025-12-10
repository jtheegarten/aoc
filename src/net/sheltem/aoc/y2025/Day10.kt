package net.sheltem.aoc.y2025

import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status
import net.sheltem.common.mapParallel
import net.sheltem.common.regex
import java.util.PriorityQueue


suspend fun main() {
    Day10().run()
}

class Day10 : Day<Long>(7, 33) {

    override suspend fun part1(input: List<String>): Long = input
        .map { it.toLightSet() }
        .mapParallel { it.bfsLights() }
        .sum()

    override suspend fun part2(input: List<String>): Long = input
        .map { it.toLightSet() }
        .mapParallel { it.solver() }
        .sum()

    private fun String.toLightSet(): LightSet =
        LightSet(
            this.substring(1, this.indexOf(']')).toBoolArray('#'),
            this.regex("\\((\\d+(\\,\\d*)*)\\)").map { it.drop(1).dropLast(1).split(",").map { num -> num.toInt() } },
            this.regex("\\{(\\d+(\\,\\d*)*)\\}").single().drop(1).dropLast(1).split(",").map { num -> num.toInt() }
        )

    private data class LightSet(val lights: List<Boolean>, val buttonSets: List<List<Int>>, val joltage: List<Int>) {

        fun bfsLights(): Long {
            val initialState = lights.map { false }
            val seen = mutableSetOf(initialState)
            val queue = PriorityQueue<Pair<List<Boolean>, Int>>(100, compareBy { it.second })
            queue.offer(initialState to 0)

            while (queue.isNotEmpty()) {
                val (state, count) = queue.poll()
                seen.add(state)
                if (state == lights) return count.toLong()

                queue.addAll(next(state, count, seen))
            }
            return -1
        }

        fun solver(): Long {
            Context().use { ctx ->
                val opt = ctx.mkOptimize()
                val presses = ctx.mkIntConst("presses")

                val buttonVars = buttonSets.indices.map { i -> ctx.mkIntConst("button $i") }

                val counters = mutableMapOf<Int, MutableList<IntExpr>>()
                buttonSets.indices.forEach { i ->
                    for (flip in buttonSets[i]) {
                        counters.computeIfAbsent(flip) { mutableListOf() }.add(buttonVars[i])
                    }
                }

                counters.forEach { (key, value) ->
                    val targetValue = ctx.mkInt(joltage[key])
                    val sumOfButtonPresses = ctx.mkAdd(*value.toTypedArray())
                    opt.Add(ctx.mkEq(targetValue, sumOfButtonPresses))
                }

                buttonVars.forEach { buttonVar ->
                    opt.Add(ctx.mkGe(buttonVar, ctx.mkInt(0)))
                }

                opt.Add(ctx.mkEq(presses, ctx.mkAdd(*buttonVars.toTypedArray())))
                opt.MkMinimize(presses)

                val status = opt.Check()
                if (status != Status.SATISFIABLE) return -1

                return (opt.model.evaluate(presses, false) as IntNum).int.toLong()
            }
        }

        fun next(state: List<Boolean>, count: Int, seen: Set<List<Boolean>>): Set<Pair<List<Boolean>, Int>> =
            buttonSets.map { buttons ->
                state.modifyWith(buttons)
            }.filter { !seen.contains(it) }
                .map { it to (count + 1) }
                .toSet()
    }

    private fun String.toBoolArray(c: Char) = this.map { it == c }
}

private fun List<Boolean>.modifyWith(buttons: List<Int>) = this
    .mapIndexed { index, bool ->
        if (buttons.contains(index)) !bool else bool
    }

private fun List<Int>.modifyJoltageWith(buttons: List<Int>) = this
    .mapIndexed { index, num ->
        if (buttons.contains(index)) num + 1 else num
    }
