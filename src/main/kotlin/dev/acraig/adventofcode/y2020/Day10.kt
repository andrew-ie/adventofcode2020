package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path
import java.util.SortedSet

data class JoltIncrements(val oneJolt: Collection<Int> = emptySet(), val threeJolt: Collection<Int> = emptySet())

fun day10Part1(input: Collection<Int>): JoltIncrements {
    val inputList = input.toList().sorted()
    return inputList.foldIndexed(JoltIncrements(threeJolt = setOf(inputList.last() + 3))) { index, previousJolts, currentValue ->
        val previousAdapter = if (index == 0) 0 else inputList[index - 1]
        when (currentValue - previousAdapter) {
            1 -> {
                previousJolts.copy(oneJolt = previousJolts.oneJolt + currentValue)
            }
            3 -> {
                previousJolts.copy(threeJolt = previousJolts.threeJolt + currentValue)
            }
            else -> {
                throw Exception("Expected 1 or 3...")
            }
        }
    }
}

fun day10Part2(input: Collection<Int>, jolts: JoltIncrements) {
    val inputSet = (input + 0)
    val joltIncrements = (jolts.threeJolt + 0).toSortedSet()
    val inputList = inputSet.toSortedSet()
    val increments = joltIncrements.zipWithNext()
    val groups = increments.map {
        inputList.subSet(it.first, it.second + 1)
    }.map { traverse(listOf(it.first()), it) }
    println(groups.fold(1L) { acc, value ->
        acc * value
    })
}

fun traverse(currentNetwork: List<Int>, inputs: SortedSet<Int>): Long {
    val currentPosition = currentNetwork.last()
    if (currentPosition == inputs.last()) {
        return 1L
    }
    val nextPossiblePositions = inputs.subSet(currentPosition + 1, currentPosition + 4)
    return nextPossiblePositions.map {
        traverse(currentNetwork + it, inputs)
    }.sum()
}

fun main() {
    val input = Files.readAllLines(Path.of("data/Day10.txt")).map { it.toInt() }
    val jolts = day10Part1(input)
    println("${jolts.oneJolt.size * jolts.threeJolt.size}")
    day10Part2(input, jolts)
}