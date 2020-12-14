package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

data class Mask(val set:Long, val unset:Long)

fun day14Part1(input:List<String>) {
    val setCommand = """mem\[(\d+)] = (\d+)""".toRegex()
    var currentMask:Mask = buildMask("0")
    val state = mutableMapOf<Long, Long>()
    input.forEach { line ->
        if (line.startsWith("mask = ")) {
            currentMask = buildMask(line.substringAfter("= "))
        } else {
            val result = setCommand.matchEntire(line)!!
            val address = result.groupValues[1].toLong()
            val update = (result.groupValues[2].toLong() or currentMask.set) and currentMask.unset
            state[address] = update
        }
    }
    println("Sum of state (Part 1): ${state.values.sum()}")
}

private fun buildMask(input:String):Mask {
    val set = input.map {
        if (it == '1') '1' else '0'
    }.joinToString("").toLong(2)
    val unset = input.map {
        if (it == '0') '0' else '1'
    }.joinToString("").toLong(2)
    return Mask(set, unset)
}

private fun applyMask(mask:String, number:Long):List<Long> {
    val numberBitField = number.toString(2).padStart(mask.length, '0')
    val values = mask.zip(numberBitField).map {
        (first, second) -> when (first) {
            '0' -> listOf(second)
            '1' -> listOf('1')
            else -> listOf('0', '1')
        }
    }
    val result = getValues(values[0], values.subList(1, values.size))
    return result.map { it.toLong(2) }
}

private tailrec fun getValues(current:List<Char>, input:List<List<Char>>, prefixes:List<String> = listOf("")):List<String> {
    val newPrefixes = prefixes.flatMap { prefix -> current.map { prefix + it } }
    return if (input.isEmpty()) {
        newPrefixes
    } else {
        val newCurrent = input[0]
        val newInput = input.subList(1, input.size)
        getValues(newCurrent, newInput, newPrefixes)
    }
}

fun day14Part2(input:List<String>) {
    val setCommand = """mem\[(\d+)] = (\d+)""".toRegex()
    var currentMask  = ""
    val state = mutableMapOf<Long, Long>()
    input.forEach { line ->
        if (line.startsWith("mask = ")) {
            currentMask = line.substringAfter("= ")
        } else {
            val result = setCommand.matchEntire(line)!!
            val addresses = applyMask(currentMask, result.groupValues[1].toLong())
            val newValue = result.groupValues[2].toLong()
            addresses.forEach {
                address -> state[address] = newValue
            }
        }
    }
    println("Sum of state (Part 2): ${state.values.sum()}")
}
fun main() {
    val input = Files.readAllLines(Path.of("data/Day14.txt"))
    day14Part1(input)
    day14Part2(input)
}