package dev.acraig.adventofcode.y2020

fun day15(startingNumbers: List<Int>, maxSize: Int): Sequence<Int> {
    var currentIndex = startingNumbers.size
    val lastSeen = IntArray(maxSize)
    startingNumbers.forEachIndexed { index, i ->
        lastSeen[i] = index + 1
    }
    return startingNumbers.subList(0, startingNumbers.size - 1).asSequence() +
            generateSequence(startingNumbers.last()) { previousNumber ->
        val nextNumber = if (lastSeen[previousNumber] != 0) currentIndex - lastSeen[previousNumber]
        else 0
        lastSeen[previousNumber] = currentIndex++
        nextNumber
    }
}

fun main() {
    val cases = listOf(
        listOf(0, 3, 6),
        listOf(1, 3, 2),
        listOf(2, 1, 3),
        listOf(1, 2, 3),
        listOf(2, 3, 1),
        listOf(3, 2, 1),
        listOf(3, 1, 2),
        listOf(7, 14, 0, 17, 11, 1, 2)
    )
    cases.forEach { case ->
        val result = day15(case, 30000000).filterIndexed { index, _ ->
            index == (2020 - 1) || index == (30000000 - 1)
        }.take(2).toList()
        println("$case -> $result")
    }
}