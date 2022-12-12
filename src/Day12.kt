class Day12 : Day<Int>(31, 29) {
    override fun part1(input: List<String>) = HeightMap(input).apply { dijkstra(this.start) }.goal.cost

    override fun part2(input: List<String>) = HeightMap(input, true).apply { dijkstra(this.start) }.goal.cost

}

fun main() {
    Day12().run()
}

class HeightMap(input: List<String>, aIsZero: Boolean = false) {
    lateinit var start: Coordinate
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

    fun dijkstra(coordinate: Coordinate) {
        coordinate.visited = true
        val cost = coordinate.cost + 1
        neighbours(coordinate).filter { it.cost > cost }.forEach { it.cost = cost }
        firstUnvisited()?.let { dijkstra(it) }
    }

    private fun neighbours(coordinate: Coordinate) = listOfNotNull(
        find(coordinate.x - 1, coordinate.y, coordinate.height),
        find(coordinate.x + 1, coordinate.y, coordinate.height),
        find(coordinate.x, coordinate.y - 1, coordinate.height),
        find(coordinate.x, coordinate.y + 1, coordinate.height),
    )

    private fun find(x: Int, y: Int, maxHeight: Int) = coordinates.find { it.x == x && it.y == y && it.height <= maxHeight + 1 }

    private fun firstUnvisited() =
        coordinates.filterNot(Coordinate::visited).minByOrNull(Coordinate::cost)
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
