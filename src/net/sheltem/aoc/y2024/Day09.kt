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

    override suspend fun part2(input: List<String>): Long =
        input[0].toComplexFileSystem()
            .let { (files, spaces, diskState) ->
                files.reversed()
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
                diskState
            }
            .withIndex()
            .sumOf { (index, value) -> index.toLong() * (value ?: 0) }
}

private fun String.toComplexFileSystem() =
    this
        .map { it.digitToInt() }
        .foldIndexed(Triple(mutableListOf<File>(), mutableListOf<Space>(), mutableListOf<Int?>())) { index, (files, spaces, diskState), size ->
            if (index % 2 == 0) {
                files.add(File(files.size, diskState.size, size))
                diskState.addAll(List(size) { files.last().id })
            } else {
                spaces.add(Space(diskState.size, size))
                diskState.addAll(List(size) { null })
            }
            Triple(files, spaces, diskState)
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
