import kotlin.math.absoluteValue

fun main() {
    Day15().run()
}

class Day15 : Day<Long>(0, 24) {

    override fun part1(input: List<String>): Long = input.map { it.toSensorAndBeacon() }.toCaveMap(2000000).countInRow(2000000).toLong()

    override fun part2(input: List<String>): Long =
        input.map { it.toSensorAndBeacon() }.findHole(4000000) //.also { println("Candidate: ${it.first}, ${it.second}") }
            .toFrequency()
}

private fun List<Pair<Pair<Int, Int>, Pair<Int, Int>>>.findHole(searchSize: Int) =
    firstNotNullOf { it.perimeter(0..searchSize).check(this) }

private fun Set<Pair<Int, Int>>.check(pairs: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Pair<Int, Int>? = firstOrNull { point -> pairs.none { it.canReach(point) } }

private fun Pair<Pair<Int, Int>, Pair<Int, Int>>.canReach(point: Pair<Int, Int>): Boolean = first.distance(second) >= first.distance(point)

private fun Pair<Pair<Int, Int>, Pair<Int, Int>>.perimeter(searchRange: IntRange): Set<Pair<Int, Int>> {
    val result = mutableSetOf<Pair<Int, Int>>()
    val sensor = this.first
    val distance = sensor.distance(this.second)
    for (diff in -(distance + 1)..(distance + 1)) {
        val counterdiff = distance + 1 - diff.absoluteValue
        val x = sensor.first + diff
        if (x in searchRange) {
            if (sensor.second - counterdiff in searchRange) result.add(x to sensor.second - counterdiff)
            if (sensor.second + counterdiff in searchRange) result.add(x to sensor.second + counterdiff)
        }
    }
    return result
}


private fun Pair<Int, Int>.toFrequency() = first * 4000000L + second

private fun Map<Pair<Int, Int>, Content>.countInRow(row: Int): Int = this.entries.count { it.key.second == row && it.value == Content.NOTHING }

private fun String.toSensorAndBeacon(): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val sensor = this.substringBefore(":").substringAfter("x=").replace("y=", "").split(",").toDataPair()
    val beacon = this.substringAfterLast("x=").replace("y=", "").split(",").toDataPair()
    return sensor to beacon
}

private fun List<Pair<Pair<Int, Int>, Pair<Int, Int>>>.toCaveMap(interestingRow: Int): Map<Pair<Int, Int>, Content> {
    val caveMap = mutableMapOf<Pair<Int, Int>, Content>()

    for (pair in this) {
        caveMap.computeContent(pair.first, pair.second, interestingRow)
    }
    return caveMap
}

private fun MutableMap<Pair<Int, Int>, Content>.computeContent(sensor: Pair<Int, Int>, beacon: Pair<Int, Int>, interestingRow: Int) {
    this[sensor] = Content.SENSOR
    this[beacon] = Content.BEACON
    val emptyPositions = spanArea(sensor, sensor.distance(beacon), interestingRow)
    for (position in emptyPositions) {
        this.putIfAbsent(position, Content.NOTHING)
    }
}

private fun spanArea(point: Pair<Int, Int>, range: Int, interestingRow: Int): List<Pair<Int, Int>> =
    buildList {
        for (x in (point.first - range)..(point.first + range)) {
            val target = x to interestingRow
            if (target.distance(point) <= range) {
                add(target)
            }
        }
    }

private fun Pair<Int, Int>.distance(other: Pair<Int, Int>) = (first - other.first).absoluteValue + (second - other.second).absoluteValue

private fun List<String>.toDataPair() = this[0].trim().toInt() to this[1].trim().toInt()

private enum class Content {
    SENSOR,
    BEACON,
    NOTHING;
}
