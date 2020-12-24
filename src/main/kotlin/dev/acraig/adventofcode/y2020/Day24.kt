package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

private data class HexPosition(val w: Int = 0, val n: Int = 0) {
    fun traverse(direction:String):HexPosition {
        return when (direction) {
            "e" -> copy(w =  w - 2)
            "w" -> copy(w = w + 2)
            "ne" -> copy(w = w - 1, n = n + 1)
            "nw" -> copy(w = w + 1, n = n + 1)
            "sw" -> copy(w = w + 1, n = n - 1)
            "se" -> copy(w = w - 1, n = n - 1)
            else ->  throw Exception("Invalid direction $direction")
        }
    }
    fun adjacentTiles():Collection<HexPosition> {
        return setOf(copy(w = w - 2), copy(w = w + 2),
            copy(w = w - 1, n = n + 1), copy(w = w + 1, n = n + 1),
            copy(w = w - 1, n = n - 1), copy(w = w + 1, n = n - 1))
    }
}

private fun traverse(initial: HexPosition, line:String):HexPosition {
    val result = generateSequence(line to initial) { (remaining, current) ->
        if (remaining.isBlank()) {
            null
        } else if (remaining[0] == 's' || remaining[0] == 'n') {
            remaining.drop(2) to current.traverse(remaining.take(2))
        } else {
            remaining.drop(1) to current.traverse(remaining.take(1))
        }
    }.map { it.second }.toList()
    return result.last()
}
private fun initialState(input:List<String>):Collection<HexPosition> {
    val start = HexPosition()
    val visited = input.map { line -> traverse(start, line) }
    val counts = visited.groupBy { it }.mapValues { (_, value) -> value.count() }
    return counts.filterValues { it % 2 == 1 }.keys
}

fun day24Part1(input:List<String>) {
    val state = initialState(input)
    println(state.size)
}
fun day24Part2(input:List<String>) {
    val state = initialState(input)
    val count = generateSequence(state) { currentState ->
        val toConsider = (currentState + currentState.flatMap { it.adjacentTiles() }).toSet()
        toConsider.filter { tile ->
            val adjacentBlackTiles = (tile.adjacentTiles() intersect currentState).size
            if (currentState.contains(tile)) { //Black
                adjacentBlackTiles == 1 || adjacentBlackTiles == 2
            } else { //White
                adjacentBlackTiles == 2
            }
        }
    }.map { it.size }.take(101).last()
    println(count)
}

fun main() {
//    val input = Files.readAllLines(Path.of("src/test/resources/day24_test.txt"))
    val input = Files.readAllLines(Path.of("data/Day24.txt"))
    day24Part1(input)
    day24Part2(input)
}
