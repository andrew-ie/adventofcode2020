package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

fun day9Part1(numbers: List<Long>, backBufferSize: Int = 25): Long {
    val data = backBufferSize..numbers.size
    val idx = data.find { candidateIndex ->
        val previous = numbers.subList(candidateIndex - backBufferSize, candidateIndex)
        val anyPossible = anyPossiblePairs(previous)
        !anyPossible.contains(numbers[candidateIndex])
    }
    return numbers[idx!!]
}

fun day9Part2(numbers: List<Long>, sum: Long): Long {
    val startNum = 0..numbers.size
    val sequence = startNum.asSequence().flatMap { start ->
        val endNum = (start + 2)..numbers.size
        endNum.asSequence().map { end -> start..end }
    }
    val seq = sequence.find {
        val toCheck = numbers.subList(it.first, it.last)
        val foundSum = toCheck.sum()
        foundSum == sum
    }
    val result = numbers.subList(seq!!.first, seq.last).toSortedSet()
    return result.first() + result.last()
}

private fun anyPossiblePairs(input: List<Long>): Collection<Long> {
    val result = input.mapIndexed { index, firstValue ->
        input.subList(index + 1, input.size).map { secondValue -> firstValue + secondValue }
    }
    return result.flatten().toSet()
}

fun main() {
    val inputs = Files.readAllLines(Path.of("data/Day9.txt")).map { it.toLong() }
    val sum = day9Part1(inputs)
    println("Part1 - $sum")
    println("Part2 - ${day9Part2(inputs, sum)}")
}