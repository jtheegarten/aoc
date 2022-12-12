import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

class Node(val name: String, outV: Set<Vertex>, inV: Set<Vertex>)

class Vertex(val weight: Long, val from: Node, val to: Node)

class Graph(val nodes: Set<Node>, val vertices: Set<Vertex>)

fun String.lastAsInt(delimiter: String) = this.split(delimiter).last().toInt()
