package net.sheltem.aoc.y2024

suspend fun main() {
    Day09().run()
}

class Day09 : Day<Long>(1928, 2858) {

    override suspend fun part1(input: List<String>): Long = input
        .toSimpleFileSystem()
        .pack()
        .filter { it >= 0 }
        .foldIndexed(0L) { index, acc, i ->
            acc + index * i
        }

    override suspend fun part2(input: List<String>): Long {
        val files = mutableListOf<File>()
        val spaces = mutableListOf<Space>()
        val diskState = mutableListOf<Int?>()
        var position = 0
        var fileId = 0

        input[0]
            .map { it.digitToInt() }
            .forEachIndexed { index, size ->
                if (index % 2 == 0) {
                    files.add(File(fileId, position, size))
                    repeat(size) {
                        diskState.add(fileId)
                    }
                    fileId++
                } else {
                    spaces.add(Space(position, size))
                    diskState.addAll(List(size) { null })
                }
                position += size
            }

        files
            .reversed()
            .forEach { file ->
                spaces
                    .withIndex()
                    .firstOrNull { (_, s) ->
                        s.start < file.start && file.size <= s.size
                    }?.let { (spaceIndex, space) ->
                        (0..<file.size).forEach { i ->
                            diskState[file.start + i] = null
                            diskState[space.start + i] = file.id
                        }
                        spaces[spaceIndex] = Space(space.start + file.size, space.size - file.size)
                    }
            }

        return diskState
            .withIndex()
            .sumOf { (index, value) -> index.toLong() * (value ?: 0) }
    }

}

private fun List<String>.toSimpleFileSystem(): MutableList<Int> = this[0]
    .mapIndexed { index, c ->
        val length = c.digitToInt()
        val id = if (index % 2 == 0) {
            index / 2
        } else {
            -1
        }
        List(length) { id }
    }.flatten().toMutableList()

private fun MutableList<Int>.pack(): MutableList<Int> {
    var front = 0
    var back = lastIndex

    while (true) {
        while (this[front] >= 0) front++
        while (this[back] < 0) back--
        if (back < front) break

        this[front] = this[back]
        this[back] = -1
    }
    return this
}

data class File(val id: Int, val start: Int, val size: Int)
data class Space(val start: Int, val size: Int)
