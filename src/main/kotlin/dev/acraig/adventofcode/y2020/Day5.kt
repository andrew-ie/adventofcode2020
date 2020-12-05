package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

fun day5(input:List<String>) {
    val seatIds = input.map(::parseBoardingPass).map(::getSeatID).toSortedSet()
    println("Last Seat ID - ${seatIds.last()}")
    val yourSeat = (seatIds.first()..seatIds.last()).filterNot { seatIds.contains(it) }
    println("Your Seat ID - $yourSeat")
}

private fun parseBoardingPass(input:String):Pair<Int, Int> {
    val row = binarySearch(input.substring(0, 7), 0 until 128, 'F')
    val column = binarySearch(input.substring(7),0 until 8, 'L')
    return row to column
}

private fun binarySearch(input:String, range:IntRange, lowerSymbol:Char):Int {
    return input.fold(range) {
        current, ch ->
        if (ch == lowerSymbol) {
            current.first..current.first + ((current.last - current.first) / 2)
        } else {
            current.first + 1 + ((current.last - current.first) / 2)..current.last
        }
    }.first
}

private fun getSeatID(input:Pair<Int, Int>):Int {
    return input.first * 8 + input.second
}

fun main() {
    //Test cases
    listOf("FBFBBFFRLR", "BFFFBBFRRR", "FFFBBBFRRR", "BBFFBBFRLL").forEach {
        println("$it - ${parseBoardingPass(it)} - ${getSeatID(parseBoardingPass(it))}")
    }
    day5(Files.readAllLines(Path.of("data", "Day5.txt")))
}