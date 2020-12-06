package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

fun day6Part1(path: Path): Int {
    return readFile(path).map { it.asSequence().filter { ch -> ch.isLetter() }.distinct().count() }.sum()
}

fun day6Part2(path: Path): Int {
    return readFile(path).map {
        it.trim().lines().foldIndexed(emptySet<Char>()) { index, acc, current ->
            val result = if (index == 0) {
                current.toSet()
            } else {
                acc intersect current.toSet()
            }
            result
        }.size
    }.sum()
}

private fun readFile(path: Path): Sequence<String> {
    val source = Files.readString(path)
    return source.splitToSequence("\n\n")
}

fun main() {
    println(day6Part1(Path.of("data", "Day6.txt")))
    println(day6Part2(Path.of("data", "Day6.txt")))
}
