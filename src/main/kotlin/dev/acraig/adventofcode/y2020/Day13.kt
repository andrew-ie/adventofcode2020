package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

data class BusOffset(val busFrequency: Long, val timetableOffset: Long) {
    fun getWait(startTime: Long): Long {
        return if (startTime % busFrequency == 0L) 0L else busFrequency - (startTime % busFrequency)
    }
}

data class Cycle(val initialOffset: Long, val repeat: Long) {
    fun sequence() = generateSequence(initialOffset) { current -> current + repeat }
    fun contains(number: Long): Boolean {
        return number % repeat == initialOffset
    }
}

private fun readBuses(input: String): List<BusOffset> {
    return input.split(",").mapIndexed { index, s ->
        if (s == "x") {
            null
        } else {
            BusOffset(s.toLong(), index.toLong())
        }
    }.filterNotNull()
}

fun day13Part1(startTime: Long, busList: List<BusOffset>) {
    val earliestBus = busList.associateBy { busNumber -> busNumber.getWait(startTime) }.minBy { it.key }!!
    println("Bus ${earliestBus.value} will be ready in ${earliestBus.key} minutes (${earliestBus.key * earliestBus.value.busFrequency})")
}

fun day13Part2(buses: List<BusOffset>) {
    val cycles = buses.map { bus ->
        val initial = generateSequence(bus.busFrequency - bus.timetableOffset) {
            it + bus.busFrequency
        }.first { it > 0L }
        Cycle(initial, bus.busFrequency)
    }
    val reduction = cycles.reduce { cycle1, cycle2 ->
        val next = cycle1.sequence().filter { cycle2.contains(it) }.take(2).toList()
        Cycle(next[0], next[1] - next[0])
    }
    println("Busses arrive in desired positioning in ${reduction.initialOffset} minutes")
}


fun main() {
    val input = Files.readAllLines(Path.of("data/Day13.txt"))
    val startTime = input[0].toLong()
    val busList = readBuses(input[1])
    day13Part1(startTime, busList)
    day13Part2(busList)
}
