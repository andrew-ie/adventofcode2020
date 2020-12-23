package dev.acraig.adventofcode.y2020

data class Circle(val contents: MutableMap<Int, Int>, var currentNode: Int) {

    fun makeMove(): Circle {
        val pickUp = traverse(currentNode, 4).drop(1)
        val destination = generateSequence(findPreviousLabel(currentNode)) {
            findPreviousLabel(it)
        }.first { !pickUp.contains(it) }
        val oldDestinationNext = contents[destination]!!
        val newResult = (pickUp.zipWithNext() + listOf(
            currentNode to contents[pickUp.last()]!!,
            destination to pickUp.first(),
            pickUp.last() to oldDestinationNext
        )).toMap()
        contents.putAll(newResult)
        currentNode = contents[currentNode]!!
        return this
    }

    fun traverse(startPoint: Int, size: Int): List<Int> {
        val seen = mutableSetOf<Int>()
        return generateSequence(startPoint) {
            contents[it]!!
        }.takeWhile { seen.add(it) }.take(size).toList()
    }

    private fun findPreviousLabel(label: Int): Int {
        return (label - 1).let {
            if (it <= 0) {
                contents.keys.max()!!
            } else it
        }
    }
}

fun day23Part1(input: String): String {
    val data = input.map { Character.getNumericValue(it) }
    val dataMap = data.zipWithNext() + listOf(data.last() to data.first())
    val sequence = generateSequence(Circle(dataMap.toMap().toMutableMap(), data[0])) {
        it.makeMove()
    }
    val result = sequence.take(101).last()
    val outcome = result.traverse(1, input.length)
    return outcome.drop(1).joinToString("")
}

fun day23Part2(input: String): Long {
    val start = input.map { (Character.getNumericValue(it)) }
    val additional = ((start.max()!! + 1)..1000000).toList()
    val data = start + additional
    val dataMap = data.zipWithNext() + listOf(data.last() to data.first())
    val result = generateSequence(Circle(dataMap.toMap().toMutableMap(), data[0])) {
        it.makeMove()
    }.take(10_000_000).last()
    val important = result.traverse(1, 3).drop(1)
    return important.fold(1L) { a, b ->
        a * b.toLong()
    }
}

fun main() {
    val part1 = day23Part1("586439172")
    val part2 = day23Part2("586439172")
    println("Part1 - $part1")
    println("Part2 - $part2")
}