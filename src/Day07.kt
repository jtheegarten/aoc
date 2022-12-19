const val maxSpace: Long = 70_000_000
const val spaceNeeded: Long = 30_000_000

fun main() {
    Day07().run()
}

class Day07 : Day<Long>(95437, 24933642) {

    override fun part1(input: List<String>): Long = input.buildTree().getAllFolders().map { it.size }.filter { it <= 100_000 }.sum()
    override fun part2(input: List<String>): Long {
        val root = input.buildTree()
        val deletionTarget = (spaceNeeded - (maxSpace - root.size))
        return root.getAllFolders().filter { it.size >= deletionTarget }.minOf { it.size }
    }
}

private fun Folder.getAllFolders(): List<Folder> = listOf(this) + content.filterIsInstance<Folder>().flatMap { it.getAllFolders() }

private fun List<String>.buildTree(): Folder = drop(1).iterator().let(Folder()::applyInstructions)

private class Folder(val content: MutableList<FSNode> = mutableListOf()) : FSNode {
    override val size: Long
        get() = content.sumOf { it.size }

    fun applyInstructions(instructions: Iterator<String>): Folder {
        while (instructions.hasNext()) {
            if (applyInstruction(instructions)) return this
        }
        return this
    }

    private fun applyInstruction(instructions: Iterator<String>) = instructions.next().let { instruction ->
        when {
            instruction == "$ cd .." -> return true
            instruction.startsWith("$ cd") -> Folder().applyInstructions(instructions).let { content.add(it) }.also { return false }
            instruction[0].isDigit() -> addFile(instruction.split(" ").first()).also { return false }
            else -> return false
        }
    }

    private fun addFile(size: String) = content.add(File(size.toLong()))
}

private class File(override val size: Long) : FSNode

interface FSNode {
    val size: Long
}
