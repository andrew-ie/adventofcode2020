package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

private val passwordCheck = """(\d+)-(\d+) (\w): (\w+)""".toRegex()
fun day2part1(input:Sequence<String>):Int {
    return input.count(::validPassword1)
}

fun day2part2(input:Sequence<String>):Int {
    return input.count(::validPassword2)
}

private fun validPassword1(input:String):Boolean {
    val match = passwordCheck.matchEntire(input)?.groupValues ?: throw IllegalArgumentException("Can't understand $input")
    val (_, min, max, expectedChar, password) = match
    val numCharacters = password.toCharArray().count { it == expectedChar[0] }
    return numCharacters >= min.toInt() && numCharacters <= max.toInt()
}

private fun validPassword2(input:String):Boolean {
    val match = passwordCheck.matchEntire(input)?.groupValues ?: throw IllegalArgumentException("Can't understand $input")
    val (_, first, second, expectedChar, password) = match
    val (firstCh, secondCh) = password.toCharArray().let { arrayOf(it[first.toInt() - 1], it[second.toInt() - 1]) }
    return (expectedChar[0] == firstCh) xor (expectedChar[0] == secondCh)
}

fun main() {
    val input = Files.lines(Path.of("data", "Day2.txt")).toList()
    println(day2part1(input.asSequence()))
    println(day2part2(input.asSequence()))
}