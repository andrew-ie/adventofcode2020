package dev.acraig.adventofcode.y2020

import java.nio.file.Files
import java.nio.file.Path

class VirtualMachine(private val commands: List<Pair<Instruction, Int>>) {
    var accumulator: Int = 0
    private var currentAddress: Int = 0
    fun run():Boolean {
        val visitedAddresses = mutableSetOf<Int>()
        while (currentAddress < commands.size && visitedAddresses.add(currentAddress)) {
            step()
        }
        return currentAddress == commands.size
    }

    private fun step(): Int {
        val currCommand = commands[currentAddress]
        when (currCommand.first) {
            Instruction.JMP -> currentAddress += currCommand.second
            Instruction.ACC -> {
                accumulator += currCommand.second
                currentAddress++
            }
            Instruction.NOP -> currentAddress++
        }
        return currentAddress
    }
}

fun day8Part1(input: Path) {
    val commands = readInstructions(input)
    val vm = VirtualMachine(commands)
    vm.run()
    println(vm.accumulator)
}


fun day8Part2(input: Path) {
    val commands = readInstructions(input)
    val vm = (commands.indices).asSequence().filter { commands[it].first != Instruction.ACC }.map { lineNum ->
        val newCommands = commands.subList(0, lineNum) + modifyInstruction(commands[lineNum]) + commands.subList(
            lineNum + 1,
            commands.size
        )
        VirtualMachine(newCommands)
    }.find {
        it.run()
    }
    println(vm?.accumulator)
}

private fun readInstructions(input: Path): List<Pair<Instruction, Int>> {
    return Files.readAllLines(input).map {
        it.split(" ")
            .let { instructionLine -> Instruction.valueOf(instructionLine[0].toUpperCase()) to instructionLine[1].toInt() }
    }
}

private fun modifyInstruction(input: Pair<Instruction, Int>): Pair<Instruction, Int> {
    return if (input.first == Instruction.JMP) {
        Instruction.NOP to input.second
    } else {
        Instruction.JMP to input.second
    }
}

enum class Instruction {
    JMP,
    ACC,
    NOP
}

fun main() {
    day8Part1(Path.of("data/Day8.txt"))
    day8Part2(Path.of("data/Day8.txt"))
}