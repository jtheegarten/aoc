import kotlin.math.absoluteValue

const val maxSpace: Long = 70_000_000
const val spaceNeeded: Long = 30_000_000

class Day07 : Day<Long>(95437, 24933642) {
    override fun part1(input: List<String>): Long = input.drop(1).iterator().buildTree().sumOfFoldersWithSizeAtMost(100000)

    override fun part2(input: List<String>): Long = input.drop(1).iterator().buildTree()
        .let { root -> root.listDeletableDirectories((spaceNeeded - (maxSpace - root.size)).absoluteValue).minBy { it.size }.size }


}

fun main() {
    Day07().run()
}

private fun Folder.listDeletableDirectories(deletionTarget: Long): List<Folder> = deletableSubdirectories(deletionTarget) + if (size >= deletionTarget) listOf(this) else listOf()

private fun Folder.deletableSubdirectories(spaceNeeded: Long) = content.filterIsInstance<Folder>().flatMap { it.listDeletableDirectories(spaceNeeded) }

private fun Folder.sumOfFoldersWithSizeAtMost(size: Long): Long = sumOfSubFolders(size) + if(this.size <= size) this.size else 0

private fun Folder.sumOfSubFolders(size: Long) = content.filterIsInstance<Folder>().sumOf { it.sumOfFoldersWithSizeAtMost(size) }

private fun Iterator<String>.buildTree(): Folder = Folder().applyInstructions(this)

private class Folder(val content: MutableList<FSNode> = mutableListOf()) : FSNode {
    override val size: Long
        get() = content.sumOf { it.size }

    fun applyInstructions(instructions: Iterator<String>): Folder {
        while (instructions.hasNext()) {
            if (applyInstruction(instructions)) {
                return this
            }
        }
        return this
    }

    private fun applyInstruction(instructions: Iterator<String>) = instructions.next().let { instruction ->
        when {
            instruction == "$ cd .." -> return true
            instruction.startsWith("$ cd") -> Folder().applyInstructions(instructions).let { content.add(it) }.also { return false }
            instruction[0].isDigit() -> instruction.split(" ").let { content.add(File(it.first().toLong())) }.also { return false }
            else -> return false
        }
    }
}

private class File(override val size: Long) : FSNode

interface FSNode {
    val size: Long
}
