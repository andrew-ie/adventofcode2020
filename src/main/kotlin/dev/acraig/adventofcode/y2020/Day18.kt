package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

fun day18Part1(input: List<String>): Long {
    return input.map { evaluateDay18(it, false) }.sum()
}

fun day18Part2(input: List<String>): Long {
    return input.map { evaluateDay18(it, true) }.sum()
}

fun evaluateDay18(string: String, applyOrdering: Boolean): Long {
    val prefix = string.substringBefore("(")
    if (prefix != string) {
        val newIndex = findCloseBracket(string, prefix)
        val bracketContents = evaluateDay18(string.drop(prefix.length + 1).take(newIndex), applyOrdering)
        val suffix = string.drop(newIndex + prefix.length + 2)
        return evaluateDay18("$prefix$bracketContents$suffix", applyOrdering)
    } else {
        var currentIndex = 0
        val tokens = generateSequence {
            if (currentIndex >= string.lastIndex) {
                null
            } else if (string[currentIndex] == '*' || string[currentIndex] == '+') {
                val result = string[currentIndex] + ""
                currentIndex++
                result
            } else {
                val result = string.drop(currentIndex).takeWhile { it != '*' && it != '+' }
                currentIndex += result.length
                result
            }
        }.toList()
        val answer = if (applyOrdering) {
            val entryToMultiply = applyResult(tokens, listOf("+"))
            applyResult(entryToMultiply, listOf("*"))
        } else {
            applyResult(tokens, listOf("+", "*"))
        }
        return answer.first().trim().toLong()
    }
}

private fun findCloseBracket(string: String, prefix: String): Int {
    var depth = 1
    val newIndex = string.drop(prefix.length + 1).indexOfFirst { ch ->
        if (ch == ')') {
            depth--
        } else if (ch == '(') {
            depth++
        }
        depth == 0
    }
    return newIndex
}

private fun applyResult(tokens: List<String>, operands: Collection<String>): List<String> {
    val doAdd = generateSequence(tokens) { currTokens ->
        val nextField = currTokens.indexOfFirst { operands.contains(it) }
        if (nextField == -1) {
            null
        } else {
            val beforeOperation = currTokens.subList(0, nextField - 1).toList()
            val afterOperation = currTokens.drop(nextField + 2)
            val firstValue = currTokens[nextField - 1].trim().toLong()
            val secondValue = currTokens[nextField + 1].trim().toLong()
            val operation = currTokens[nextField]
            val output = if (operation == "*") {
                firstValue * secondValue
            } else {
                firstValue + secondValue
            }
            val result = beforeOperation + output.toString() + afterOperation
            result
        }
    }
    return doAdd.last()
}

fun main() {
    val input = Files.readAllLines(Path.of("data/Day18.txt"))
    println("Part1: ${day18Part1(input)}")
    println("Part2: ${day18Part2(input)}")
}