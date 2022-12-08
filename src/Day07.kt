const val maxSpace: Long = 70_000_000
const val spaceNeeded: Long = 30_000_000

class Day07 : Day<Long>(95437, 24933642) {
    override fun part1(input: List<String>): Long = input.drop(1).iterator().buildTree().getAllFolders().map { it.size }.filter { it <= 100_000 }.sum()

    override fun part2(input: List<String>): Long {
        val root = input.drop(1).iterator().buildTree()
        val deletionTarget = (spaceNeeded - (maxSpace - root.size))
        return root.getAllFolders().filter { it.size >= deletionTarget }.minOf { it.size }
    }
}

fun main() {
    Day07().run()
}

private fun Folder.getAllFolders(): List<Folder> = listOf(this) + content.filterIsInstance<Folder>().flatMap { it.getAllFolders() }

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
