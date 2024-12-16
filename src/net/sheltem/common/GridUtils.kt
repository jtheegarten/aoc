package net.sheltem.common

import java.util.function.Predicate
import kotlin.math.abs

typealias PositionInt = Pair<Int, Int>
typealias Position = Pair<Long, Long>

val PositionInt.x get() = first
val PositionInt.y get() = second

enum class Direction(val coords: PositionInt) {
    NORTH(0 to -1),
    EAST(1 to 0),
    SOUTH(0 to 1),
    WEST(-1 to 0),
    NEUTRAL(0 to 0);

    fun turnRight() = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
        else -> NORTH
    }

    fun turnLeft() = when (this) {
        NORTH -> WEST
        EAST -> NORTH
        SOUTH -> EAST
        WEST -> SOUTH
        else -> NORTH
    }

    companion object {
        val cardinals = listOf(NORTH, EAST, SOUTH, WEST)

        fun from(dirString: String): Direction = when (dirString) {
            "U", "^", "N" -> NORTH
            "D", "v", "S" -> SOUTH
            "L", "<", "W" -> WEST
            "R", ">", "E" -> EAST
            else -> NEUTRAL
        }
    }
}

enum class Direction8(val coords: PositionInt) {
    NORTH(0 to -1),
    NORTH_EAST(1 to -1),
    EAST(1 to 0),
    SOUTH_EAST(1 to 1),
    SOUTH(0 to 1),
    SOUTH_WEST(-1 to 1),
    WEST(-1 to 0),
    NORTH_WEST(-1 to -1),
    NEUTRAL(0 to 0);

    val longCoords = coords.first.toLong() to coords.second.toLong()

    companion object {
        val cardinals = listOf(NORTH, EAST, SOUTH, WEST)

        fun fromCaret(caret: Char): Direction8 = fromCaret(caret.toString())

        fun fromCaret(caret: String): Direction8 = when (caret) {
            "U", "^", "N" -> NORTH
            "D", "v", "S" -> SOUTH
            "L", "<", "W" -> WEST
            "R", ">", "E" -> EAST
            else -> error("Not a valid caret")
        }
    }
}

data class Grid<T>(
    private val list: List<MutableList<T>>
) : Iterable<Pair<PositionInt, T>> {

    companion object {
        fun fromStrings(list: List<String>) = Grid(list.filter { it.isNotBlank() }.map { it.toMutableList() })
    }

    val maxRow = list.size - 1
    val maxCol = list.first().size - 1

    val allCoordinates: List<PositionInt>
        get() = list.indices.flatMap { y -> list.first().indices.map { x -> x to y } }

    val allFields: List<T>
        get() = list.flatten()

    operator fun contains(pos: PositionInt) = pos.x in list.first().indices && pos.y in list.indices
    operator fun get(pos: PositionInt) = if(contains(pos)) list[pos.y][pos.x] else null
    operator fun set(pos: PositionInt, value: T) {
        list[pos.y][pos.x] = value
    }

    fun getRow(index: Int) = list[index]
    fun getColumn(index: Int) = list.map { it[index] }

    fun <P> transform(block: (PositionInt, T) -> P): Grid<P> =
        list.mapIndexed { y, row ->
            row.mapIndexed { x, data ->
                block(x to y, data)
            }.toMutableList()
        }.let(::Grid)

    fun print(toString: (PositionInt) -> Any = { pos -> this[pos] ?: "" }) {
        list.indices.forEach { y ->
            list.first().indices.forEach { x ->
                print(toString(x to y))
            }
            println()
        }
    }

    fun find(item: T) = allCoordinates.first { this[it] == item }

    fun corners(pos: PositionInt) = (Direction.entries - Direction.NEUTRAL + Direction.NORTH).zipWithNext()
        .filter { (d1, d2) ->
            val a = this[pos move d1]
            val b = this[pos move d2]
            val c = this[pos]
            (a != c && b != c) || (a == c && b == c && this[pos move d1 move d2] != c)
        }.size

    override fun iterator(): Iterator<Pair<PositionInt, T>> = allCoordinates.map { it to this[it]!! }.iterator()
}
fun List<String>.toGrid() = Grid.fromStrings(this)
fun List<PositionInt>.takeWord(grid: Grid<Char>): String = mapNotNull { grid[it] }.joinToString("")

fun PositionInt.lineTo(direction: Direction, length: Int) = (0..length).map { this.move(direction, it) }
fun PositionInt.lineTo(direction: Direction8, length: Int) = (0..length).map { this.move(direction, it) }

fun List<PositionInt>.takeWord(charMap: List<String>): String = map { charMap.charAtOrNull(it) }.joinToString("")

fun String.toDirection() = Direction.from(this)

fun Pair<Long, Long>.neighbours(other: Collection<Direction8>): List<Pair<Long, Long>> = other.map { it.coords.first + first to it.coords.second + second }
fun Pair<Long, Long>.neighbour(other: Direction8): Pair<Long, Long> = this.first + other.coords.first to this.second + other.coords.second
fun Collection<Pair<Long, Long>>.bounds() = (minOf { it.first } to minOf { it.second }) to (maxOf { it.first } to maxOf { it.second })

fun PositionInt.within(map: List<CharSequence>) = this.first in map[0].indices && this.second in map.indices
fun PositionInt.withinMap(map: List<List<*>>) = this.first in map[0].indices && this.second in map.indices
fun PositionInt.withinArrayMap(map: List<IntArray>) = this.first in map[0].indices && this.second in map.indices

fun PositionInt.neighbours(predicate: Predicate<PositionInt> = Predicate { true }) =
    (Direction.entries - Direction.NEUTRAL).map { this.move(it) }.filter { predicate.test(it) }

fun PositionInt.move(direction: Direction8, distance: Int = 1) = this + (direction.coords * distance)
fun PositionInt.move(direction: Direction, distance: Int = 1) = this + (direction.coords * distance)
infix fun PositionInt.move(direction: Direction) = this + direction.coords
infix fun PositionInt.move(direction8: Direction8) = this + direction8.coords

fun Pair<PositionInt, PositionInt>.manhattan(): Long = (abs(this.first.first - this.second.first) + abs(this.first.second - this.second.second)).toLong()
infix fun PositionInt.manhattan(other: PositionInt): Long = (this to other).manhattan()

fun List<PositionInt>.gaussArea(): Long {
    val last = this.last()
    val first = this.first()
    val area = (0..<indices.last).fold(0L) { acc, i ->
        acc + this[i].first.toLong() * this[i + 1].second - this[i + 1].first.toLong() * this[i].second
    } + last.first.toLong() * first.second - first.first.toLong() * last.second

    return abs(area) / 2
}

fun List<String>.charAtOrNull(pos: PositionInt) = if (pos.within(this)) this[pos.second][pos.first] else null
operator fun List<String>.get(pos: PositionInt) = this.charAtOrNull(pos)
fun List<String>.charAt(pos: PositionInt) = this.charAtOrNull(pos)!!
fun List<String>.corners(pos: PositionInt) = (Direction.entries - Direction.NEUTRAL + Direction.NORTH).zipWithNext()
    .filter { (d1, d2) ->
        val a = this.charAtOrNull(pos.move(d1))
        val b = this.charAtOrNull(pos.move(d2))
        val c = this.charAtOrNull(pos)
        (a != c && b != c) || (a == c && b == c && this.charAtOrNull(pos.move(d1).move(d2)) != c)
    }.size

fun List<String>.replace(pos: PositionInt, char: Char) = this.mapIndexed { y, row -> if (y == pos.second) row.replaceCharAt(pos.first, char) else row }
fun List<String>.find(char: Char) = indices.flatMap { y -> this[y].indices.map { x -> x to y } }.first { (x, y) -> this[y][x] == char }
fun String.replaceCharAt(index: Int, char: Char): String = this.take(index) + char + this.drop(index + 1)

fun List<IntArray>.intAt(pos: PositionInt) = if (pos.withinArrayMap(this)) this[pos.second][pos.first] else null

operator fun PositionInt.plus(other: PositionInt) = (this.first + other.first) to (this.second + other.second)
operator fun PositionInt.minus(other: PositionInt) = (this.first - other.first) to (this.second - other.second)
operator fun PositionInt.times(mult: Int) = (this.first * mult) to (this.second * mult)
