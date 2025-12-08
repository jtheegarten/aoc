package net.sheltem.common

import kotlin.math.pow
import kotlin.math.sqrt

typealias Point3D = Triple<Long, Long, Long>

fun String.toPoint3D() = split(Regex("[,\\s]")).map { it.toLong() }.let { (a, b, c) -> Point3D(a, b, c)}

fun Point3D.bigDistance(b: Point3D): Double = (this.first - b.first).toDouble().pow(2) + (this.second - b.second).toDouble().pow(2) + (this.third - b.third).toDouble().pow(2)
fun Point3D.distance(b: Point3D): Double = sqrt(this.bigDistance(b))
fun Point3D.sum() = first + second + third
