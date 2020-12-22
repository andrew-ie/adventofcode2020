package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

private fun readPlayers(input: String): List<List<Long>> {
    val playerInput = input.split("\n\n")
    return playerInput.map { block ->
        val list = block.split("\n")
        val cards = list.drop(1).map { it.trim() }.filter { it.isNotBlank() }.map { it.toLong() }
        cards
    }
}

private fun playGame(startingPoint: List<List<Long>>, recursive: Boolean = false): Pair<Int, List<Long>> {
    val previousRounds = mutableSetOf<List<List<Long>>>()
    val rounds = generateSequence(startingPoint) { currentRound ->
        if (currentRound.any { it.isEmpty() } || !previousRounds.add(currentRound)) {
            null
        } else {
            val round = currentRound.map { it.first() }
            val nextCards = currentRound.map { cards -> cards.drop(1) }
            val winner = if (recursive && round.zip(currentRound).all { (cardId, cards) ->
                    (cards.size) > cardId
                }) {
                playGame(
                    nextCards.mapIndexed { index, cardset -> cardset.take(round[index].toInt()) },
                    true
                ).first
            } else if (round[0] < round[1]) {
                1
            } else {
                0
            }
            val toAdd = if (winner == 0) round else round.reversed()
            nextCards.mapIndexed { index, list -> if (index == winner) list + toAdd else list }
        }
    }
    val winningRound = rounds.last()
    return if (winningRound.any { it.isEmpty() }) {
        val winner = winningRound.indexOfFirst { it.isNotEmpty() }
        winner to winningRound[winner]
    } else {
        0 to winningRound[0]
    }
}

fun day22Part1(input: String) {
    val players = readPlayers(input)
    val result = playGame(players)
    val winner = result.first
    val score = calculateScore(result.second)
    println("Crab Combat Winner: $winner, score: $score")
}

fun day22Part2(input: String) {
    val players = readPlayers(input)
    val result = playGame(players, true)
    val winner = result.first
    val score = calculateScore(result.second)
    println("Recursive Crab Combat Winner: $winner, score: $score")
}

private fun calculateScore(order: List<Long>) = order.foldIndexed(0L) { index, acc, l ->
    val multiplier = order.size - index
    (l * multiplier) + acc
}

fun main() {
//    val data = Files.readString(Path.of("src/test/resources/day22_test.txt"))
    val data = Files.readString(Path.of("data/Day22.txt"))
    day22Part1(data)
    day22Part2(data)
//    println(day22Part1(Files.readString(Path.of("data/Day22.txt"))))
}