class Day06 : Day<Int>("06", 7, 19) {
    override fun part1(input: List<String>): Int = input.first().indexOfMarker()

    override fun part2(input: List<String>): Int = input.first().indexOfMarker(14)

}

fun main() {
    Day06().run()
}

private fun String.indexOfMarker(length: Int = 4): Int {
    for (i in 3 until (this.length - 3)) {
        val chunk = subSequence(i, i + length)
        if (chunk.allUnique()) return i + length
    }
    return 0
}

private fun CharSequence.allUnique(): Boolean = toSet().size == length
