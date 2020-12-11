package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

enum class SeatState(val type: Char) {
    OCCUPIED('#'),
    EMPTY('L'),
    FLOOR('.');

    companion object {
        fun parse(ch: Char): SeatState {
            return values().find { it.type == ch } ?: throw IllegalArgumentException("Unrecognized type $ch")
        }
    }
}

enum class Direction {
    UP {
        override fun move(position: Position): Position {
            return position.copy(col = position.col + 1)
        }
    },
    DOWN {
        override fun move(position: Position): Position {
            return position.copy(col = position.col - 1)
        }
    },
    LEFT {
        override fun move(position: Position): Position {
            return position.copy(row = position.row - 1)
        }
    },
    RIGHT {
        override fun move(position: Position): Position {
            return position.copy(row = position.row + 1)
        }
    },
    DIAGONAL_UP_LEFT {
        override fun move(position: Position): Position {
            return position.copy(row = position.row + 1, col = position.col - 1)
        }
    },
    DIAGONAL_UP_RIGHT {
        override fun move(position: Position): Position {
            return position.copy(row = position.row + 1, col = position.col + 1)
        }
    },
    DIAGONAL_DOWN_LEFT {
        override fun move(position: Position): Position {
            return position.copy(row = position.row - 1, col = position.col - 1)
        }
    },
    DIAGONAL_DOWN_RIGHT {
        override fun move(position: Position): Position {
            return position.copy(row = position.row - 1, col = position.col + 1)
        }
    };

    abstract fun move(position: Position): Position
}

data class Position(val row: Int, val col: Int)
data class Boat(val seats: Map<Position, SeatState>) {
    private val rowMax = seats.keys.map { it.row }.max()!!
    private val colMax = seats.keys.map { it.col }.max()!!

    fun createNewStatePart1(): Boat {
        return Boat(seats.mapValues { (position, currState) ->
            val adjacent = Direction.values().map { it.move(position) }.mapNotNull { seats[it] }
            if (currState == SeatState.EMPTY && !adjacent.contains(SeatState.OCCUPIED)) {
                SeatState.OCCUPIED
            } else if (currState == SeatState.OCCUPIED && adjacent.count { it == SeatState.OCCUPIED } >= 4) {
                SeatState.EMPTY
            } else {
                currState
            }
        })
    }

    fun createNewStatePart2(): Boat {
        return Boat(seats.mapValues { (position, currState) ->
            val adjacent = Direction.values().mapNotNull { findVisible(it, position) }
            if (currState == SeatState.EMPTY && !adjacent.contains(SeatState.OCCUPIED)) {
                SeatState.OCCUPIED
            } else if (currState == SeatState.OCCUPIED && adjacent.count { it == SeatState.OCCUPIED } >= 5) {
                SeatState.EMPTY
            } else {
                currState
            }
        })
    }

    private fun findVisible(direction: Direction, position: Position): SeatState? {
        val initial = direction.move(position)
        if (initial.row < 0 || initial.col < 0 || initial.row > rowMax || initial.col > colMax) {
            return null
        }
        val seq = generateSequence(initial) {
            val newPosition = direction.move(it)
            if (newPosition.row < 0 || newPosition.col < 0 || newPosition.row > rowMax || newPosition.col > colMax) {
                null
            } else {
                newPosition
            }
        }
        return seq.mapNotNull { seats[it] }.find { it != SeatState.FLOOR }
    }

}

private fun readInput(lines: List<String>): Boat {
    val seats = lines.mapIndexed { row, line ->
        line.mapIndexed { col, ch ->
            Position(row, col) to SeatState.parse(ch)
        }
    }
    return Boat(seats.flatten().toMap())
}

fun day11Part1(input: List<String>) {
    val result = generateSequence(readInput(input)) { previous ->
        val next = previous.createNewStatePart1()
        if (next == previous) {
            null
        } else {
            next
        }
    }.last()
    val occupiedSeats = result.seats.values.count { it == SeatState.OCCUPIED }
    println("Part1 - $occupiedSeats seats occupied")
}

fun day11Part2(input: List<String>) {
    val result = generateSequence(readInput(input)) { previous ->
        val next = previous.createNewStatePart2()
        if (next == previous) {
            null
        } else {
            next
        }
    }.last()
    val occupiedSeats = result.seats.values.count { it == SeatState.OCCUPIED }
    println("Part2 - $occupiedSeats seats occupied")
}

fun main() {
//    val lines = Files.readAllLines(Path.of("src/test/resources/day11_test.txt"))
    val lines = Files.readAllLines(Path.of("data/Day11.txt"))
    day11Part1(lines)
    day11Part2(lines)
}