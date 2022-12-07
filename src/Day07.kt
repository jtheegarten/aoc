import kotlin.math.absoluteValue

const val maxSpace: Long = 70_000_000
const val spaceNeeded: Long = 30_000_000

class Day07 : Day<Long>("07", 95437, 24933642) {
    override fun part1(input: List<String>): Long = input.iterator().buildTree().sumOfFoldersWithSizeAtMost(100000)

    override fun part2(input: List<String>): Long {
        val root = input.iterator().buildTree()
        val deletionTarget = (spaceNeeded - (maxSpace - root.size)).absoluteValue
        val directoriesList = root.listDeletableDirectories(deletionTarget)
        return directoriesList.minBy { it.size }.size
    }


}

private fun Folder.listDeletableDirectories(deletionTarget: Long): List<Folder> =
    if (size >= deletionTarget) listOf(this) + deletableSubdirectories(deletionTarget)
    else deletableSubdirectories(deletionTarget)

private fun Folder.deletableSubdirectories(spaceNeeded: Long) = content.filterIsInstance<Folder>().flatMap { it.listDeletableDirectories(spaceNeeded) }

fun main() {
    Day07().run()
}

fun Folder.sumOfFoldersWithSizeAtMost(size: Long): Long =
    if (this.size <= size) this.size + sumOfSubFolders(size)
    else sumOfSubFolders(size)

private fun Folder.sumOfSubFolders(size: Long) = content.filterIsInstance<Folder>().sumOf { it.sumOfFoldersWithSizeAtMost(size) }

fun Iterator<String>.buildTree(): Folder {
    next()
    return Folder("/").applyInstructions(this)
}


class Folder(val name: String, val content: MutableList<FSNode> = mutableListOf()) : FSNode {
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

    private fun applyInstruction(instructions: Iterator<String>): Boolean {
        val instruction = instructions.next()
        when {
            instruction == "$ cd .." -> return true
            instruction.startsWith("$ cd") -> Folder(instruction.substring(5)).applyInstructions(instructions).let { content.add(it) }
            instruction[0].isDigit() -> instruction.split(" ").let { (size, name) -> content.add(File(name, size.toLong())) }
            else -> return false
        }
        return false
    }

    override fun toString() = "Folder $name of size $size with content:\n\n" + content.joinToString("\n") { " - \t ${it.toString()}" }
}

class File(val name: String, override val size: Long) : FSNode {
    override fun toString() = "File $name of size $size"
}

interface FSNode {
    val size: Long
}
