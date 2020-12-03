package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

data class Person(val position: Int = 0, val treesEncountered: Int = 0)

fun day3Part1(treeCoordinates: List<List<Int>>): Int {
    return traverse(treeCoordinates, 3)
}

fun day3Part2(treeCoordinates: List<List<Int>>): Long {
    val runs = listOf(
        1 to 1,
        3 to 1,
        5 to 1,
        7 to 1,
        1 to 2
    )
    return runs.fold(1L) { acc, pair ->
        acc * traverse(treeCoordinates, pair.first, pair.second)
    }
}

private fun traverse(
    treeCoordinates: List<List<Int>>,
    rightOffset: Int,
    downOffset: Int = 1
): Int {
    val result = treeCoordinates.filterIndexed { index, _ -> index != 0 && index % downOffset == 0 }
        .fold(Person()) { person, row ->
            val newPosition = (person.position + rightOffset) % row.size
            val newPerson = Person(newPosition, person.treesEncountered + row[newPosition])
            newPerson
        }
    return result.treesEncountered
}

fun loadMap(path: Path?): List<List<Int>> {
    return Files.readAllLines(path).map { row ->
        row.map {
            if (it == '.') {
                0
            } else {
                1
            }
        }
    }
}

fun main() {
    val path = Path.of("data", "Day3.txt")
    val treeCoordinates = loadMap(path)
    println(day3Part1(treeCoordinates))
    println(day3Part2(treeCoordinates))
}