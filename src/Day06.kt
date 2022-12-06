class Day06 : Day<Int>("06", 7, 19) {
    override fun part1(input: List<String>): Int = input.first().indexOfMarker()

    override fun part2(input: List<String>): Int = input.first().indexOfMarker(14)

}

fun main() {
    Day06().run()
}

private fun String.indexOfMarker(length: Int = 4) = windowed(length, 1).map { it.allUnique() }.indexOf(true) + length

private fun CharSequence.allUnique(): Boolean = toSet().size == length
