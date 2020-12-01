package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

fun day1Part1(input:Set<Long>):Long? {
    val entry = input.find {
        firstHalf -> input.contains(2020L - firstHalf)
    }
    return entry?.times(2020L - entry)
}

fun day1Part2(input:Set<Long>):Long? {
    val pairs = input.flatMap { firstValue -> input.filter { it != firstValue }.map { listOf(firstValue, it) } }
    val entry = pairs.find { firstTwo -> input.contains(2020L - firstTwo.sum()) }
    return entry?.plus(2020L - entry.sum())?.foldRight(1L) { a, b -> a * b }
}

fun main() {
    val input = Files.lines(Path.of("data", "Day1Part1.txt")).mapToLong{ it.toLong() } .boxed().collect(Collectors.toSet())
    println(day1Part1(input))
    println(day1Part2(input))
}
