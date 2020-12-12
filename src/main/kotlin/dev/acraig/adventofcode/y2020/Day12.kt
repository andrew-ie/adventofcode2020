package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.absoluteValue

private data class Ship(val xPosition: Int = 0, val yPosition: Int = 0, val waypoint: Waypoint = Waypoint()) {
    fun movePart1(input:String):Ship {
        val amount = input.substring(1, input.length).toInt()
        return when (input[0]) {
            'N' -> copy(yPosition = yPosition + amount)
            'S' -> copy(yPosition = yPosition - amount)
            'E' -> copy(xPosition = xPosition + amount)
            'W' -> copy(xPosition = xPosition - amount)
            'L','R' -> copy(waypoint = waypoint.move(input[0], amount))
            'F' -> moveForwards(amount)
            else -> throw IllegalArgumentException("Unrecognized symbol $input")
        }
    }

    fun movePart2(input:String):Ship {
        val amount = input.substring(1, input.length).toInt()
        return when (input[0]) {
            'N', 'S', 'E', 'W', 'L', 'R' -> copy(waypoint = waypoint.move(input[0], amount))
            'F' -> moveForwards(amount)
            else -> throw IllegalArgumentException("Unrecognized command $input")
        }
    }

    private fun moveForwards(amount:Int):Ship {
        return copy(xPosition = xPosition + (waypoint.xPosition * amount), yPosition = yPosition + (waypoint.yPosition * amount) )
    }
}
private data class Waypoint(val xPosition: Int = 10, val yPosition: Int = 1) {
    fun move(code:Char, amount:Int):Waypoint {
        return when (code) {
            'N' -> copy(yPosition = yPosition + amount)
            'S' -> copy(yPosition = yPosition - amount)
            'E' -> copy(xPosition = xPosition + amount)
            'W' -> copy(xPosition = xPosition - amount)
            'L' -> rotate(360 - amount)
            'R' -> rotate(amount)
            else -> throw IllegalArgumentException("Unrecognized command $code")
        }
    }
    private fun rotate(amount:Int):Waypoint {
        return when (amount) {
            90 -> copy(xPosition = yPosition, yPosition = xPosition * -1)
            180 -> copy(xPosition = xPosition * -1, yPosition = yPosition * -1)
            270 -> copy(xPosition = yPosition * -1, yPosition = xPosition)
            0 -> this
            else -> throw IllegalArgumentException("Can't rotate to position $amount")
        }
    }
}

fun day12Part1(input:List<String>) {
    val newPosition = input.fold(Ship(waypoint = Waypoint(1, 0))) {
        currPosition, command -> currPosition.movePart1(command)
    }
    println("Part1 - New Position $newPosition, Distance away: ${newPosition.xPosition.absoluteValue + newPosition.yPosition.absoluteValue}")
}

fun day12Part2(input: List<String>) {
    val newPosition = input.fold(Ship()) {
        currPosition, command -> currPosition.movePart2(command)
    }
    println("Part2 - New Position $newPosition, Distance away: ${newPosition.xPosition.absoluteValue + newPosition.yPosition.absoluteValue}")
}
fun main() {
    val input = Files.readAllLines(Path.of("Data/Day12.txt"))
    day12Part1(input)
    day12Part2(input)
}