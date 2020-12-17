package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

private data class Cube(val position:List<Int>) {
    fun getNeighbours(): Collection<Cube> {
        return genCubes(emptyList(), position) - this
    }
    private fun genCubes(prefix:List<Int>, remainingPosition: List<Int>):Collection<Cube> {
        return if (remainingPosition.isNotEmpty()) {
            (-1..1).flatMap { genCubes(prefix + (remainingPosition[0] + it), remainingPosition.drop(1)) }
        } else {
            listOf(Cube(prefix))
        }
    }
}

private fun boot(initial: Collection<Cube>): Int {
    val sequence = generateSequence(initial) { current ->
        val toConsider = current.flatMap { it.getNeighbours() }.toSet()
        val active = toConsider.filter { cube ->
            (current.contains(cube) && (cube.getNeighbours() intersect current).size in 2..3) ||
                    (!current.contains(cube) && (cube.getNeighbours() intersect current).size == 3)
        }.toSet()
        active
    }
    return sequence.take(7).last().size
}

fun day17Part1(input:List<String>) {
    val data = read(input, 3)
    println(boot(data))
}
fun day17Part2(input:List<String>) {
    val data = read(input, 4)
    println(boot(data))
}

private fun read(input:List<String>, dimensions:Int):Collection<Cube> {
    val padding = List(dimensions - 2) {0}
    return input.mapIndexed { y, line ->
        line.mapIndexed { x, ch ->
            if (ch == '#') Cube(listOf(x, y) + padding) else null
        }.filterNotNull()
    }.flatten().toSet()
}

fun main() {
    val input = Files.readAllLines(Path.of("data/Day17.txt"))
    day17Part1(input)
    day17Part2(input)
}