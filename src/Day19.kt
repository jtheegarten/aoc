import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun main() {
    Day19().run()
}

class Day19 : Day<Int>(33, 3472) {

    override fun part1(input: List<String>): Int = runBlocking(Dispatchers.Default) {
        input
            .map { it.toBlueprint() }
            .mapParallel { it.toMaxGeodes(robots = intArrayOf(1, 0, 0, 0)) }
            .mapIndexed { i, quality -> (i + 1) * quality }
            .sum()
    }

    override fun part2(input: List<String>): Int = runBlocking(Dispatchers.Default) {
        input
            .map { it.toBlueprint() }
            .take(3)
            .mapParallel { it.toMaxGeodes(robots = intArrayOf(1, 0, 0, 0), time = 32) }
            .reduce { acc, i -> acc * i }
    }
}

private fun String.toBlueprint(): List<Robot> = Regex("\\d+")
    .findAll(this)
    .toList()
    .map { it.value.toInt() }
    .let { listOf(Robot(it[1]), Robot(it[2]), Robot(it[3], it[4]), Robot(it[5], 0, it[6])) }

private fun List<Robot>.toMaxGeodes(resources: IntArray = IntArray(4), robots: IntArray = IntArray(4), time: Int = 24, maxGeodes: Int = 0): Int {
    if ((0 until time).sumOf { it } + robots[3] * time + resources[3] <= maxGeodes) return 0

    val maxPrices = listOf(maxOf { it.ore }, maxOf { it.clay }, maxOf { it.obsidian }, 9999)

    var currentResources: IntArray
    var currentRobots: IntArray
    var max = resources[3] + robots[3] * time
    var wait: Int

    for (i in indices) {
        if (robots[i] >= maxPrices[i]) continue
        if (i == 2 && robots[1] == 0) continue
        if (i == 3 && robots[2] == 0) continue
        wait = this[i].resourceWait(resources, robots) + 1

        if (time - wait < 1) continue

        currentResources = resources.clone()
        currentRobots = robots.clone()
        for (j in resources.indices) currentResources[j] += robots[j] * wait
        currentResources[0] -= this[i].ore
        currentResources[1] -= this[i].clay
        currentResources[2] -= this[i].obsidian

        currentRobots[i]++

        max = max.coerceAtLeast(this.toMaxGeodes(currentResources, currentRobots, time - wait, max))
    }

    return max
}

private data class Robot(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0) {
    fun resourceWait(resources: IntArray, robots: IntArray): Int {
        val costs = listOf(ore, clay, obsidian)
        var maxTime = 0
        for (i in costs.indices) {
            if (costs[i] == 0) continue
            if (costs[i] > 0 && robots[i] == 0) return 9999
            maxTime = maxTime.coerceAtLeast((costs[i] - resources[i] - 1 + robots[i]) / robots[i])
        }
        return maxTime
    }
}
