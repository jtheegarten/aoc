class Day12 : Day<Int>(31, 29) {
    override fun part1(input: List<String>) = HeightMap(input).dijkstra().goal.cost

    override fun part2(input: List<String>) = HeightMap(input, true).dijkstra().goal.cost

}

fun main() {
    Day12().run()
}

class HeightMap(input: List<String>, aIsZero: Boolean = false) {
    private lateinit var start: Coordinate
    lateinit var goal: Coordinate
    private val coordinates: List<Coordinate> =
        input.mapIndexed { y, row ->
            row.mapIndexed { x, char ->
                Coordinate(x, y, char.height()).apply {
                    if (char == 'S' || (aIsZero && height == 0)) cost = 0
                    if (char == 'S') start = this
                    if (char == 'E') goal = this
                }
            }
        }.flatten()

    fun dijkstra(coordinate: Coordinate = start): HeightMap = this.apply {
        coordinate.visited = true
        val cost = coordinate.cost + 1
        coordinate.neighbours().filter { it.cost > cost }.forEach { it.cost = cost }
        coordinates.filterNot { it.visited }.minByOrNull { it.cost }?.let { dijkstra(it) }
    }

    private fun Coordinate.neighbours() = listOfNotNull(
        find(x - 1, y, height),
        find(x + 1, y, height),
        find(x, y - 1, height),
        find(x, y + 1, height),
    )

    private fun find(x: Int, y: Int, maxHeight: Int) = coordinates.find { it.x == x && it.y == y && it.height <= maxHeight + 1 }

}

class Coordinate(
    val x: Int,
    val y: Int,
    val height: Int,
    var cost: Int = Int.MAX_VALUE,
    var visited: Boolean = false
)

private fun Char.height() = when (this) {
    'S' -> 0
    'E' -> 26
    else -> this - 'a'
}
